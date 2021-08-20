package example

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset
import io.micronaut.configuration.kafka.annotation.Topic
import java.util.*

@KafkaListener(offsetReset = OffsetReset.EARLIEST)
class JobListener(private val secondService: SecondService) {

    @Topic("second-service-jobs")
    fun receive(value: UUID) {
        secondService.execute(value.toString())
    }
}
