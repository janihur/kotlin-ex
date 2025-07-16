package net.metsankulma.jokes.service

import net.metsankulma.jokes.dto.out.Joke
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.sql.Statement

@Service
class JokesDb(private val db: JdbcTemplate) {
    class JokeRowMapper : RowMapper<Joke> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Joke {
            return Joke(
                rs.getInt("id").toUInt(),
                rs.getString("id_ext"),
                rs.getString("family"),
                rs.getString("text")
            )
        }
    }

    private val jokeRowMapper = JokeRowMapper()

    fun get(id: Int): Joke? {
        return db.query(
            "select id, id_ext, family, text from jokes where id = ?",
            jokeRowMapper,
            id
        ).singleOrNull()
    }

    // get a random joke (for family)
    fun getRandom(family: String? = null): Joke? {
        return if (family != null) {
            db.query(
                "select id, id_ext, family, text from jokes where family = ? order by random() limit 1",
                jokeRowMapper,
                family
            ).singleOrNull()
        } else {
            db.query(
                "select id, id_ext, family, text from jokes order by random() limit 1",
                jokeRowMapper
            ).singleOrNull()
        }
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