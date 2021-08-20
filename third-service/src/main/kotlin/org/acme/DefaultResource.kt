package org.acme

import org.jboss.logging.Logger
import java.time.Instant
import java.util.*
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/default")
class DefaultResource(private val log: Logger) {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    fun run(@HeaderParam("x-correlation-id") correlationIdString: String): String {
        doSomeLogic()
        saveData(correlationIdString)
        // callNextService()
        withProbability(0.1) { throw RuntimeException() }
        return "Hello RESTEasy"
    }

    private fun doSomeLogic() {
        Thread.sleep(1000)
    }

    private fun saveData(correlationIdString: String) {
        val third = Third()
        third.correlationId = correlationIdString
        third.ctime = Instant.now()
        third.persist()
        log.infov("Saved data, correlation id={0}", correlationIdString)
    }
}

private fun withProbability(prob: Double, callback: () -> Unit) {
    if (Math.random() < prob) {
        callback()
    }
}
