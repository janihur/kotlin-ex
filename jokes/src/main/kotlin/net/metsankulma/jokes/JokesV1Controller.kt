package net.metsankulma.jokes

import net.metsankulma.jokes.dto.ErrorResponse
import net.metsankulma.jokes.service.BaconIpsumApi
import net.metsankulma.jokes.service.ChuckNorrisApi
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
class JokesV1Controller(private var service: JokesService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/bacon")
    @Suppress("unused")
    fun getBaconIpsum(): ResponseEntity<List<String>> {
        val bacon = BaconIpsumApi().get()
        return ResponseEntity.ok(bacon)
    }

    // get a random joke for family
    @GetMapping("/")
    @Suppress("unused")
    fun getJoke(@RequestParam(required = true) family: String, @RequestParam amount: Int = 1): ResponseEntity<Any> {
        logger.info("One joke requested for family: '$family', amount: $amount")
        // allow only certain family values
        val allowedFamilies = setOf("bacon", "chucknorris", "dadjoke", "official", "simpsons")
        if (family !in allowedFamilies) {
            val error = ErrorResponse(
                httpStatusCode = 400,
                message = "Invalid 'family' parameter.",
                details = mapOf(
                    "receivedValue" to family,
                    "allowedValues" to allowedFamilies)
            )
            logger.warn(error.toString())
            return ResponseEntity.badRequest().body(error)
        }
        // allow only certain amount values
        if (amount < 1 || amount > 10) {
            val error = ErrorResponse(
                httpStatusCode = 400,
                message = "Invalid 'amount' parameter.",
                details = mapOf(
                    "receivedValue" to amount,
                    "allowedValues" to "1-10"
                )
            )
            logger.warn(error.toString())
            return ResponseEntity.badRequest().body(error)
        }

        val joke = when (family) {
            "bacon" -> {
                logger.info("Fetching a random bacon.")
                val bacon = BaconIpsumApi().get()
                Joke(family = family, text = bacon.first())
            }
            "chucknorris" -> {
                logger.info("Fetching a random Chuck Norris fact.")
                val fact = ChuckNorrisApi().get()
                Joke(family = family, text = fact)
            }
            "dadjoke" -> {
                logger.info("Fetching a random dad joke.")
                Joke(family = family, text = "TODO")
            }
            "official" -> {
                logger.info("Fetching a random official joke.")
                val joke = OfficialJokeApi().get()
                logger.debug("Received joke: $joke")
                Joke(family = family, text = "TODO")
            }
            "simpsons" -> {
                logger.info("Fetching a random Simpsons quote.")
                Joke(family = family, text = "TODO")
            }
            else -> {
                logger.error("Unexpected family value: $family")
                Joke(family = family, text = "Never heard of it!")
            }
        }

        return ResponseEntity.ok(joke)
    }

    @GetMapping("joke/{id}")
    @Suppress("unused")
    fun getJokeById(@PathVariable id: Int): ResponseEntity<Joke> {
        logger.info("Fetching joke with id: $id")
        val joke = service.get(id)
        return if (joke != null) {
            logger.debug("Found joke: $joke")
            ResponseEntity.ok(joke)
        } else {
            logger.warn("Joke with id $id not found.")
            ResponseEntity.notFound().build()
        }
    }

    data class ImportResponse(val location: String)

    @PostMapping("/import")
    @Suppress("unused")
    fun importJokes(@RequestBody jokes: List<Joke>): ResponseEntity<List<ImportResponse>> {
        logger.info("Received batch of ${jokes.size} jokes for import.")

        if (jokes.isEmpty()) {
            logger.debug("No jokes provided for import.")
            return ResponseEntity.badRequest().body(emptyList())
        }

        val response = mutableListOf<ImportResponse>()

        for (joke in jokes) {
            logger.info("Processing joke: $joke")
            val savedJoke = service.save(joke)
            logger.debug("Saved joke: $savedJoke")
            response += ImportResponse("${ApiPaths.JOKES_V1}/${savedJoke.id}")
        }

        return ResponseEntity.created(URI(ApiPaths.JOKES_V1)).body(response)
    }
}