package net.metsankulma.jokes.dto

data class ErrorResponse (
    val httpStatusCode: Int,
    val message: String,
    val details: Map<String, Any> = emptyMap()
)