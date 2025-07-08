package net.metsankulma.jokes

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Service
import java.sql.Statement

@Service
class JokesService(private val db: JdbcTemplate) {
    fun getRandomJoke(): Joke? {
        return db.query("select * from jokes order by random() limit 1") { response, _ ->
            Joke(response.getObject("id") as UInt?, response.getString("family"), response.getString("text"))
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

        // convert to UInt? to match your model
        // note there is 3 generated keys // (ID, FAMILY, TEXT) and we only need the ID
        val id = keyHolder.keys?.get("ID")?.toString()?.toLong()?.toUInt()
        return joke.copy(id = id)
    }
}