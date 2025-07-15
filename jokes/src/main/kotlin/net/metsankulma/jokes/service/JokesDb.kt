package net.metsankulma.jokes.service

import net.metsankulma.jokes.dto.out.Joke
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Service
import java.sql.Statement

@Service
class JokesDb(private val db: JdbcTemplate) {
    fun get(id: Int): Joke? {
        return db.query("select id, id_ext, family, text from jokes where id = ?", id) { response, _ ->
            Joke(
                response.getInt("id").toUInt(),
                response.getString("id_ext"),
                response.getString("family"),
                response.getString("text")
            )
        }.singleOrNull()
    }

    // get a random joke for family
    fun getRandom(family: String): Joke? {
        return db.query(
            "select id, id_ext, family, text from jokes where family = ? order by random() limit 1",
            family
        ) { response, _ ->
            Joke(
                response.getInt("id").toUInt(),
                response.getString("id_ext"),
                response.getString("family"),
                response.getString("text")
            )
        }.singleOrNull()
    }

    fun save(joke: Joke): Joke {
        val keyHolder = GeneratedKeyHolder()

        db.update({ connection ->
            val ps = connection.prepareStatement(
                "INSERT INTO jokes (family, text) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
            )
            ps.setString(1, joke.family)
            ps.setString(2, joke.text)
            ps
        }, keyHolder)

        // convert id to UInt to match your model
        // note there is 3 generated keys (ID, CREATED, UPDATED) but we only need the ID
        val id = keyHolder.keys?.get("ID")?.toString()?.toLong()?.toUInt()
        return joke.copy(id = id)
    }
}