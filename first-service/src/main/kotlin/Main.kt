import io.javalin.Javalin
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.UUIDSerializer
import org.apache.kafka.common.serialization.VoidSerializer
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Instant
import java.util.*

val logger: Logger = LoggerFactory.getLogger("first-service")

fun main() {

    Database.connect(
        "jdbc:mysql://user:password@localhost/microservices-study",
        driver = "com.mysql.cj.jdbc.Driver"
    )
    transaction {
        SchemaUtils.drop(First)
        SchemaUtils.create(First)
    }

    val kafkaProducer = createKafkaProducer()

    val app = Javalin.create().start(7001)
    app.get("/") { ctx ->
        ctx.result("Hello World")
        transaction {
            doSomeLogic()
            val correlationIdString = saveData()
//            callNextServiceSync(correlationIdString)
            callNextServiceAsync(correlationIdString, kafkaProducer)
            withProbability(0.1) { throw RuntimeException() }
        }
    }
}

private fun doSomeLogic() {
    Thread.sleep(1000)
}

private fun createKafkaProducer(): KafkaProducer<Void, UUID> {
    val properties = Properties()
    properties[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
    return KafkaProducer(properties, VoidSerializer(), UUIDSerializer())
}

private fun saveData(): String {
    val correlationIdString = UUID.randomUUID().toString()
    First.insert {
        it[correlationId] = correlationIdString
        it[ctime] = Instant.now()
    }
    logger.info("Saved data, correlation id={}", correlationIdString)
    return correlationIdString
}

private fun callNextServiceSync(correlationIdString: String) {
    val client = HttpClient.newBuilder().build();
    val request =
        HttpRequest.newBuilder(URI("http://localhost:7002/"))
            .header("x-correlation-id", correlationIdString)
            .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    logger.info(
        "Called second-service, status code={}, correlation id={}",
        response.statusCode(),
        correlationIdString
    )
}

private fun callNextServiceAsync(
    correlationIdString: String,
    kafkaProducer: KafkaProducer<Void, UUID>
) {
    val record = ProducerRecord<Void, UUID>(
        "second-service-jobs",
        null,
        UUID.fromString(correlationIdString)
    )
    kafkaProducer.send(record)
    kafkaProducer.flush()
    logger.info("Message sent to second-service, correlation id={}", correlationIdString)
}

private fun withProbability(prob: Double, callback: () -> Unit) {
    if (Math.random() < prob) {
        callback()
    }
}

object First : IntIdTable() {
    val correlationId = varchar("correlation_id", 36)
    val ctime = timestamp("ctime")
}
