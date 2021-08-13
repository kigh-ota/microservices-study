package example

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import java.time.LocalDateTime
import java.util.*

@Controller("/")
class DefaultController(private val secondRepository: SecondRepository) {
    @Get(produces = [MediaType.TEXT_PLAIN])
    fun index(@QueryValue firstUuidString: String): String {
        val firstUuid = UUID.fromString(firstUuidString)
        val second = Second(0, UUID.randomUUID(), firstUuid, LocalDateTime.now())
        secondRepository.save(second)
        return "Hello World"
    }
}
