import io.javalin.Javalin
import io.javalin.core.util.Header
import io.javalin.http.Context
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.`java-time`.datetime
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
import java.time.LocalDateTime
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

    val app = Javalin.create().start(7001)
    app.get("/") {ctx ->
        ctx.result("Hello World")
        transaction {
            doSomeLogic()
            val correlationIdString = saveData()
            callNextService(correlationIdString)
            withProbability(0.1) { throw RuntimeException() }
        }
    }
}

private fun doSomeLogic() {
    Thread.sleep(1000)
}

private fun saveData(): String {
    val correlationIdString = UUID.randomUUID().toString()
    First.insert {
        it[correlationId] = correlationIdString
        it[ctime] = Instant.now()
    }
    logger.info("Saved data, correlation id={}",correlationIdString)
    return correlationIdString
}

private fun callNextService(correlationIdString: String) {
    val client = HttpClient.newBuilder().build();
    val request =
        HttpRequest.newBuilder(URI("http://localhost:7002/"))
            .header("x-correlation-id", correlationIdString)
            .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    logger.info("Called second-service, status code={}, correlation id={}", response.statusCode(), correlationIdString)
}

private fun withProbability(prob: Double, callback: () -> Unit) {
    if (Math.random() < prob) {
        callback()
    }
}

object First: IntIdTable() {
    val correlationId = varchar("correlation_id", 36)
    val ctime = timestamp("ctime")
}
