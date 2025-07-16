package net.metsankulma.jokes.controller

import net.metsankulma.jokes.ApiPaths
import net.metsankulma.jokes.dto.Message
import net.metsankulma.jokes.service.MessagesDb
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping(ApiPaths.MESSAGES_V1)
class MessagesV1Api(private val messagesDb: MessagesDb) {

    @GetMapping
    fun listMessages() = messagesDb.findMessages()

    @GetMapping("/{id}")
    fun getMessage(@PathVariable id: String): ResponseEntity<Message> =
        messagesDb.findMessageById(id).toResponseEntity()

    @PostMapping
    fun post(@RequestBody message: Message): ResponseEntity<Message> {
        val savedMessage = messagesDb.save(message)
        return ResponseEntity
            .created(URI("${ApiPaths.MESSAGES_V1}/${savedMessage.id}"))
            .body(savedMessage)
    }

    private fun Message?.toResponseEntity(): ResponseEntity<Message> =
        // If the message is null (not found), set response code to 404
        // this?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
        when (this) {
            is Message -> ResponseEntity.ok(this)
            else -> ResponseEntity.notFound().build()
        }
}