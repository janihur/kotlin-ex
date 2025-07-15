package net.metsankulma.jokes.dto.out

data class Joke(
    val id: UInt? = null,
    val extId: String? = null,
    val family: String,
    val text: String
)