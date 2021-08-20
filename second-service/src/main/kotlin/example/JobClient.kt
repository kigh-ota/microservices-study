package example

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic
import java.util.*

@KafkaClient
interface JobClient {
    @Topic("third-service-jobs")
    fun sendJob(value: UUID);
}
