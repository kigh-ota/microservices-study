package example

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

@Controller("/")
class DefaultController(private val secondService: SecondService) {
    private val log = LoggerFactory.getLogger("second-service")

    @Get(produces = [MediaType.TEXT_PLAIN])
    fun index(@Header("x-correlation-id") correlationIdString: String): String {
        secondService.execute(correlationIdString)
        return "Hello World"
    }
}
