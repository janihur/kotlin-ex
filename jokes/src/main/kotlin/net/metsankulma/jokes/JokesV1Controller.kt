package net.metsankulma.jokes

import net.metsankulma.jokes.dto.out.BAD_REQUEST
import net.metsankulma.jokes.dto.out.GONE
import net.metsankulma.jokes.dto.out.INTERNAL_SERVER_ERROR
import net.metsankulma.jokes.dto.out.ImportResponse
import net.metsankulma.jokes.dto.out.Joke
import net.metsankulma.jokes.dto.out.NOT_FOUND
import net.metsankulma.jokes.dto.out.NOT_IMPLEMENTED
import net.metsankulma.jokes.dto.out.OK
import net.metsankulma.jokes.service.BaconIpsumApi
import net.metsankulma.jokes.service.ChuckNorrisApi
import net.metsankulma.jokes.service.JokesDb
import net.metsankulma.jokes.service.OfficialJokeApi
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping(ApiPaths.JOKES_V1)
class JokesV1Controller(private var jokesDb: JokesDb) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val allowedFamilies = setOf("bacon", "chucknorris", "dadjoke", "mystery", "official", "simpsons")

    // ------------------------------------------------------------------------
    // get a random joke for family from internet
    @GetMapping("/")
    @Suppress("unused")
    fun getJoke(
        @RequestParam(required = true) family: String,
        @RequestParam amount: Int = 1
    ): ResponseEntity<Any> {
        logger.info("One joke requested for family: '$family', amount: $amount")

        // allow only certain amount values
        if (amount < 1 || amount > 10) {
            val response = BAD_REQUEST(
                message = "Invalid 'amount' parameter.",
                details = mapOf("receivedValue" to amount, "allowedValues" to "1-10")
            )
            logger.info(response.toString())
            response
        }

        // resolve family to a specific joke type
        return when (family) {
            "bacon" -> {
                logger.info("Fetching a random bacon.")
                val bacon = BaconIpsumApi().get()

                val response = OK(family = family, text = bacon.first())
                logger.info(response.toString())
                response
            }
            "chucknorris" -> {
                logger.info("Fetching a random Chuck Norris fact.")
                val fact = ChuckNorrisApi().get()

                val response = OK(family = family, text = fact)
                logger.info(response.toString())
                response
            }
            "dadjoke" -> {
                logger.info("Fetching a random dad joke.")

                val response = NOT_IMPLEMENTED(details = mapOf("family" to family))
                logger.info(response.toString())
                response
            }
            "mystery" -> {
                logger.info("Fetching a random mystery joke.")

                val response = NOT_IMPLEMENTED(details = mapOf("family" to family))
                logger.info(response.toString())
                response
            }
            "official" -> {
                logger.info("Fetching a random official joke.")
                val joke = OfficialJokeApi().get()
                logger.debug("Received joke: $joke")

                val (response, loggerF) = if (joke != null) {
                    Pair<ResponseEntity<Any>, (String)-> Unit>(
                        OK(family = family, text = "${joke.setup} ${joke.punchline}"),
                        logger::info
                    )
                } else {
                    Pair<ResponseEntity<Any>, (String)-> Unit>(
                        INTERNAL_SERVER_ERROR(details = mapOf("family" to family)),
                        logger::error
                    )
                }
                loggerF(response.toString())
                response
            }
            "simpsons" -> {
                logger.info("Fetching a random Simpsons quote.")

                val response =
                GONE(
                    message = "Simpsons quotes are no longer available. See https://blog.glitch.com/post/changes-are-coming-to-glitch/",
                    details = mapOf("family" to family)
                )
                logger.info(response.toString())
                response
            }
            else -> {
//                val allowedFamilies = setOf("bacon", "chucknorris", "dadjoke", "mystery", "official", "simpsons")
                val response = BAD_REQUEST(
                    message = "Invalid 'family' query parameter.",
                    details = mapOf("receivedValue" to family, "allowedValues" to allowedFamilies)
                )
                logger.info(response.toString())
                response
            }
        }
    }

    // ------------------------------------------------------------------------
    // get a joke from local database by id
    @GetMapping("/localdb/{id}")
    @Suppress("unused")
    fun getJokeById(@PathVariable(required = true) id: Int): ResponseEntity<Any> {
        logger.info("Fetching joke with id: $id")
        val joke = jokesDb.get(id)
        return if (joke != null) {
            logger.debug("Found joke: $joke")
            OK(joke)
        } else {
            logger.debug("Joke with id $id not found.")
            NOT_FOUND(details = mapOf("id" to id))
        }
    }

    // ------------------------------------------------------------------------
    // get a random joke from local database for family
    @GetMapping("/localdb/random")
    @Suppress("unused")
    fun getRandomJoke(
        @RequestParam(required = true) family: String
    ): ResponseEntity<Any> {
        logger.info("Fetching a random joke for family: '$family'")

        // resolve family to a specific joke type
        return when {
            family in allowedFamilies -> {
                val joke = jokesDb.getRandom(family)
                if (joke != null) {
                    logger.debug("Found joke: $joke")
                    OK(joke)
                } else {
                    logger.debug("No jokes found for family: $family")
                    NOT_FOUND(details = mapOf("family" to family))
                }
            }
            else -> {
                val response = BAD_REQUEST(
                    message = "Invalid 'family' query parameter.",
                    details = mapOf("receivedValue" to family, "allowedValues" to allowedFamilies)
                )
                logger.info(response.toString())
                response
            }
        }
    }
    // ------------------------------------------------------------------------
    // add a batch of jokes to local database
    @PostMapping("/localdb/import")
    @Suppress("unused")
    fun importJokes(@RequestBody jokes: List<Joke>): ResponseEntity<List<ImportResponse>> {
        logger.info("Received batch of ${jokes.size} jokes for import.")

        val location = "${ApiPaths.JOKES_V1}/localdb/"
        val response = mutableListOf<ImportResponse>()

        for (joke in jokes) {
            logger.info("Processing joke: $joke")
            val savedJoke = jokesDb.save(joke)
            logger.debug("Saved joke with id: ${savedJoke.id}")
            response += ImportResponse("$location/${savedJoke.id}")
        }

        return ResponseEntity.created(URI(location)).body(response)
    }
}