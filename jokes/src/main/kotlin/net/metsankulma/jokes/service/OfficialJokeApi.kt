package net.metsankulma.jokes.service

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class OfficialJokeApi {
    private val client: WebClient = WebClient.builder()
        .baseUrl("https://official-joke-api.appspot.com/jokes")
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .build()

    // TODO this is blocking version, make also non-blocking version
    // TODO marshall to data class
    fun get(): String {
        return client.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/random")
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
            .block() // blocks until the response is received
            ?: ""
    }

}