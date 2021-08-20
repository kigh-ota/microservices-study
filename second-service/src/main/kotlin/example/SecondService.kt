package example

import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class SecondService(
    private val secondRepository: SecondRepository,
    private val thirdServiceClient: ThirdServiceClient,
    private val jobClient: JobClient
) {
    private val log = LoggerFactory.getLogger("SecondService")

    @Transactional
    open fun execute(correlationIdString: String) {
        doSomeLogic()
        saveData(correlationIdString)
//        callNextServiceSync(correlationIdString)
        callNextServiceAsync(correlationIdString)
        withProbability(0.1) { throw RuntimeException() }
    }

    private fun doSomeLogic() {
        Thread.sleep(1000)
    }

    private fun saveData(correlationIdString: String) {
        val second = Second(0, UUID.fromString(correlationIdString), Instant.now())
        secondRepository.save(second)
        log.info("Saved data, correlation id={}", correlationIdString)
    }

    private fun callNextServiceSync(correlationIdString: String) {
        val statusCode = thirdServiceClient.default(correlationIdString)
        log.info(
            "Called third-service, status code={}, correlation id={}",
            statusCode,
            correlationIdString
        )
    }

    private fun callNextServiceAsync(correlationIdString: String) {
        jobClient.sendJob(UUID.fromString(correlationIdString))
        log.info("Message sent to third-service, correlation id={}", correlationIdString)
    }
}

private fun withProbability(prob: Double, callback: () -> Unit) {
    if (Math.random() < prob) {
        callback()
    }
}
