package example

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import java.net.URI
import javax.inject.Singleton

@Singleton
class ThirdServiceClient(@param:Client("http://localhost:7003") private val httpClient: HttpClient) {
    private val uri: URI = UriBuilder.of("/default").build()

    fun default(correlationIdString: String): Int {
        val req = HttpRequest.GET<Any>(uri)
            .header("x-correlation-id", correlationIdString)
        val res: HttpResponse<Any> = httpClient.toBlocking().exchange(req)
        return res.status.code
    }
}
