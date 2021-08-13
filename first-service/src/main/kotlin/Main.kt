import io.javalin.Javalin
import io.javalin.http.Context
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

fun main() {
    Database.connect(
        "jdbc:mysql://user:password@localhost/microservices-study",
        driver = "com.mysql.cj.jdbc.Driver"
    )
    transaction {
        SchemaUtils.create(First)
    }

    val app = Javalin.create().start(7001)
    app.get("/") {ctx ->
        ctx.result("Hello World")
        transaction {
            First.insert {
                it[uuid] = UUID.randomUUID()
                it[ctime] = LocalDateTime.now()
            }
            withProbability(0.1) { throw RuntimeException() }
        }
    }
}

private fun withProbability(prob: Double, callback: () -> Unit) {
    if (Math.random() < prob) {
        callback()
    }
}

object First: IntIdTable() {
    val uuid = uuid("uuid")
    val ctime = datetime("ctime")
}
