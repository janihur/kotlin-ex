package net.metsankulma.jokes.service

import net.metsankulma.jokes.dto.`in`.OfficialJoke
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
    // TODO error reporting/handling
    fun get(): OfficialJoke? {
        return client.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/random")
                    .build()
            }
            .retrieve()
            .bodyToMono<OfficialJoke>() // automatically converts JSON to OfficialJoke data class
            .block() // blocks until the response is received
            ?: null
    }

}