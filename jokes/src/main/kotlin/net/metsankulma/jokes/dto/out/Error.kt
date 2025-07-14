package net.metsankulma.jokes.dto.out

data class Error (
    val httpStatusCode: Int,
    val message: String,
    val details: Map<String, Any> = emptyMap()
)