package example

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

@Controller("/")
class DefaultController(private val secondRepository: SecondRepository,
                        private val thirdServiceClient: ThirdServiceClient) {
    private val log = LoggerFactory.getLogger("second-service")

    @Get(produces = [MediaType.TEXT_PLAIN])
    fun index(@Header("x-correlation-id") correlationIdString: String): String {
        doSomeLogic()
        saveData(correlationIdString)
        callNextService(correlationIdString)
        withProbability(0.1) { throw RuntimeException() }
        return "Hello World"
    }

    private fun doSomeLogic() {
        Thread.sleep(1000)
    }

    private fun saveData(correlationIdString: String) {
        val second = Second(0, UUID.fromString(correlationIdString), Instant.now())
        secondRepository.save(second)
        log.info("Saved data, correlation id={}",correlationIdString)
    }

    private fun callNextService(correlationIdString: String) {
        val statusCode = thirdServiceClient.default(correlationIdString)
        log.info("Called third-service, status code={}, correlation id={}", statusCode, correlationIdString)
    }
}

private fun withProbability(prob: Double, callback: () -> Unit) {
    if (Math.random() < prob) {
        callback()
    }
}
