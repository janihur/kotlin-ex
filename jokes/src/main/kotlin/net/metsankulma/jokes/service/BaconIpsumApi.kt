package net.metsankulma.jokes.service

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class BaconIpsumApi {
    private val client: WebClient = WebClient.builder()
        .baseUrl("https://baconipsum.com/api/")
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .build()

    // TODO this is blocking version, make also non-blocking version
    fun get(): List<String> {
        return client.get()
            .uri { uriBuilder ->
                uriBuilder
                    .queryParam("format", "json")
                    .queryParam("type", "all-meat")
                    .queryParam("start-with-lorem", 1)
                    .queryParam("sentences", 1)
                    .build()
            }
            .retrieve()
            .bodyToMono<List<String>>()
            .block() // blocks until the response is received
            ?: emptyList()
    }
}