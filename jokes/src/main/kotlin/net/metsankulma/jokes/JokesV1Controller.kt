package net.metsankulma.jokes

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping(ApiPaths.JOKES_V1)
class JokesV1Controller(private var service: JokesService) {

    private val logger = LoggerFactory.getLogger(JokesV1Controller::class.java)

    @GetMapping("/{id}")
    fun listJokes(@PathVariable id: Int): Joke {
        return Joke(family = "dadjokes", text= listOf(
            "Why don't scientists trust atoms? Because they make up everything.",
            "Why did the scarecrow win an award? Because he was outstanding in his field.",
            "I told my wife she was drawing her eyebrows too high. She looked surprised.",
            "Parallel lines have so much in common. It’s a shame they’ll never meet.",
            "Why don’t skeletons fight each other? They don’t have the guts",
            "I used to play piano by ear, but now I use my hands.",
            "Why did the bicycle fall over? Because it was two-tired.",
            "Why did the math book look sad? Because it had so many problems.",
            "Did you hear about the claustrophobic astronaut? He just needed a little space.",
            "Why did the golfer bring two pairs of pants? In case he got a hole in one.",
            "I used to be a baker, but I couldn’t make enough dough.",
            "Why did the tomato turn red? Because it saw the salad dressing.",
            "I’m reading a book on anti-gravity. It’s impossible to put down.",
            "Why did the coffee file a police report? It got mugged.",
            "I told my friend 10 jokes to make him laugh. Sadly, no pun in ten did.",
            "Why don’t scientists trust stairs? Because they’re always up to something!",
            "Why did the chicken join a band? Because it had the drumsticks!",
        )[id])
    }

    data class ImportResponse(val location: String)

    @PostMapping("/import")
    fun importJokes(@RequestBody jokes: List<Joke>): ResponseEntity<List<ImportResponse>> {
        logger.info("Received batch of ${jokes.size} jokes for import.")

        if (jokes.isEmpty()) {
            logger.debug("No jokes provided for import.")
            return ResponseEntity.badRequest().body(emptyList())
        }

        val response = mutableListOf<ImportResponse>()

        for (joke in jokes) {
            logger.info("Processing joke: $joke")
            // TODO service throws exception if joke is invalid
            val savedJoke = service.save(joke)
            logger.debug("Saved joke: $savedJoke")
            response += ImportResponse("${ApiPaths.JOKES_V1}/${savedJoke.id}")
        }

        return ResponseEntity.created(URI(ApiPaths.JOKES_V1)).body(response)
    }
}