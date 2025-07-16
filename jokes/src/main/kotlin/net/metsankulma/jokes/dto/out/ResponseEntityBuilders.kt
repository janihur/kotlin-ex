package net.metsankulma.jokes.dto.out

import org.springframework.http.ResponseEntity
import java.net.URI

fun OK(
    family: String,
    text: String
): ResponseEntity<Any> {
    return ResponseEntity
        .ok()
        .body(Joke(family = family, text = text))
}

fun OK(
    joke: Joke
): ResponseEntity<Any> {
    return ResponseEntity
        .ok()
        .body(joke)
}

fun CREATED(
    location: String,
    locations: List<Location>
): ResponseEntity<List<Location>> {
    return ResponseEntity
        .created(URI(location))
        .body(locations)
}

fun BAD_REQUEST(
    message: String = "The request could not be understood by the server due to malformed syntax.",
    details: Map<String, Any> = emptyMap()
): ResponseEntity<Any> {
    return ResponseEntity
        .badRequest()
        .body(Error(400, message, details))
}

fun NOT_FOUND(
    message: String = "The requested resource could not be found.",
    details: Map<String, Any> = emptyMap()
): ResponseEntity<Any> {
    return ResponseEntity
        .status(404)
        .body(Error(404, message, details))
}

fun GONE (
    message: String = "The requested resource is no longer available.",
    details: Map<String, Any> = emptyMap()
): ResponseEntity<Any> {
    return ResponseEntity
        .status(410)
        .body(Error(410, message, details))
}

fun INTERNAL_SERVER_ERROR(
    message: String = "An internal server error occurred.",
    details: Map<String, Any> = emptyMap()
): ResponseEntity<Any> {
    return ResponseEntity
        .status(500)
        .body(Error(500, message, details))
}

fun NOT_IMPLEMENTED(
    message: String = "The requested resource is not implemented.",
    details: Map<String, Any> = emptyMap()
): ResponseEntity<Any> {
    return ResponseEntity
        .status(501)
        .body(Error(501, message, details))
}