package net.metsankulma.jokes.service

import net.metsankulma.jokes.dto.Message
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MessagesDb(private val db: JdbcTemplate) {
    fun findMessages(): List<Message> = db.query(
        "select * from messages"
    ) { response, _ -> Message(response.getString("id"), response.getString("text")) }

    fun findMessageById(id: String): Message? = db.query(
        "select * from messages where id = ?",
        id
    ) { response, _ -> Message(response.getString("id"), response.getString("text")) }
        .singleOrNull()

    fun save(message: Message): Message {
        val id = message.id ?: UUID.randomUUID().toString() // Generate new id if it is null
        db.update(
            "insert into messages values (?,?)",
            id, message.text
        )
        return message.copy(id = id) // Return a copy of the message with the new id
    }
}