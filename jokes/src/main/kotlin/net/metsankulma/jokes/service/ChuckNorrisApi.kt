package net.metsankulma.jokes.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class ChuckNorrisApi {
    private val client: WebClient = WebClient.builder()
        .baseUrl("https://api.chucknorris.io/jokes")
        .build()

    // TODO this is blocking version, make also non-blocking version
    // Read the answer as a String and parse it as JSON.
    fun get(): Pair<String, String> {
        val jsonResponse = client.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/random")
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
            .block() // blocks until the response is received

        val rootNode = jacksonObjectMapper().readTree(jsonResponse)
        return Pair<String, String>(
            rootNode.get("id")?.asText() ?: "",
            rootNode.get("value")?.asText() ?: "No fact available."
        )
    }
}