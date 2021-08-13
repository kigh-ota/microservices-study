package example

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.QueryValue
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Controller("/")
class DefaultController(private val secondRepository: SecondRepository) {
    @Get(produces = [MediaType.TEXT_PLAIN])
    fun index(@Header("x-correlation-id") correlationIdString: String): String {
        val second = Second(0, UUID.fromString(correlationIdString), Instant.now())
        secondRepository.save(second)
        return "Hello World"
    }
}
