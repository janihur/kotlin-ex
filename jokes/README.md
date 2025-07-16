# jokes


An example Kotlin Spring Boot API application with H2 database backend.

Originally https://kotlinlang.org/docs/jvm-get-started-spring-boot.html but modified after that.

Initial application skeleton created by: https://start.spring.io/

Run the application in the IDE:
```
Go to file: JokesApplication.kt
 > Shift + F10
```

## Packages

The source code is shared into following packages:

|Package                        |Description|
|-------------------------------|-----------|
|`net.metsankulma.jokes.dto.in` |Incoming [data transfer objects](https://en.wikipedia.org/wiki/Data_transfer_object).|
|`net.metsankulma.jokes.dto.out`|Outgoing [data transfer objects](https://en.wikipedia.org/wiki/Data_transfer_object).|
|`net.metsankulma.jokes.service`|[Annotated "Service" classes](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html).|

## API

The application hosts both the original messages API and later added jokes API.

|API Endpoint           |Description|
|-----------------------|-----------|
|`GET /messages/v1`     |List all messages from local database.|
|`GET /messages/v1/{id}`|Get a message from the local database by `{id}`.|
|`POST /messages/v1`    |Add a message to the local database.|

|API Endpoint                   |Description|
|-------------------------------|-----------|
|`GET /jokes/v1/localdb/{id}`   |Get a joke from the local database by `{id}`.|
|`GET /jokes/v1/localdb/random` |Get a random joke from the local database.|
|`POST /jokes/v1/localdb/import`|Add a batch of jokes to the local database.|
|`GET /jokes/v1/`               |Get a random joke from internet.|

Unexpected problems are covered automatically by the web framework as [500 Internal Server Error](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status/500).

## Used 3rd Party APIs

* https://baconipsum.com/
* https://api.chucknorris.io/
* https://github.com/15Dkatz/official_joke_api

Previously used The Simpsons quote API was discontinued on 8th July 2025 as glitch.com ended project hosting.

Examples:
```
curl 'https://baconipsum.com/api/?type=all-meat&sentences=1'
["Pastrami ball tip porchetta flank pork loin drumstick."]

curl https://api.chucknorris.io/jokes/random
{
  "categories": [],
  "created_at": "2020-01-05 13:42:20.262289",
  "icon_url": "https://api.chucknorris.io/img/avatar/chuck-norris.png",
  "id": "lxZodJ3LTvWrqiJIRrx21w",
  "updated_at": "2020-01-05 13:42:20.262289",
  "url": "https://api.chucknorris.io/jokes/lxZodJ3LTvWrqiJIRrx21w",
  "value": "Chuck Norris got tired of hearing about the internet phenomenon about him. So he recently invented the Chuckroll."
}

curl https://official-joke-api.appspot.com/jokes/random
{
  "type":"general",
  "setup":"Have you heard of the band 1023MB?",
  "punchline":"They haven't got a gig yet.",
  "id":110
}
```

## TODO

HTTP calls greetings:
```
curl --verbose http://localhost:8080
curl --verbose http://localhost:8080/ID

curl -X POST --location \
 http://localhost:8080 \
 -H 'Content-Type: application/json' \
 -d '{ "text": "Hello!" }'

curl -X POST --location \
 http://localhost:8080 \
 -H 'Content-Type: application/json' \
 -d '{ "text": "Bonjour!" }'

curl -X POST --location \
 http://localhost:8080 \
 -H 'Content-Type: application/json' \
 -d '{ "text": "Ciao!" }'
```

HTTP calls jokes:
```
curl --verbose http://localhost:8080/jokes/v1
```

```
curl --verbose http://localhost:8080/jokes/v1/import \
 -X POST \
 --header 'Content-Type: application/json' \
 --data "[{\"family\": \"dadjoke\", \"text\": \"Why don't scientists trust atoms? Because they make up everything.\"}]"

curl --verbose http://localhost:8080/jokes/v1/import \
 -X POST \
 --header 'Content-Type: application/json' \
 --data "[{\"family\": \"dadjoke\", \"text\": \"Why did the scarecrow win an award? Because he was outstanding in his field.\"},
{\"family\": \"dadjoke\", \"text\": \"Why did the tomato turn red? Because it saw the salad dressing.\"}]"

curl --verbose http://localhost:8080/jokes/v1/localdb/6

curl --verbose 'http://localhost:8080/jokes/v1?family=dadjoke&amount=2'
```

## Dad Jokes

Curated list of high quality Dad Jokes.
```
"Did you hear about the claustrophobic astronaut? He just needed a little space.",
"I told my friend 10 jokes to make him laugh. Sadly, no pun in ten did.",
"I told my wife she was drawing her eyebrows too high. She looked surprised.",
"I used to be a baker, but I couldn’t make enough dough.",
"I used to play piano by ear, but now I use my hands.",
"I’m reading a book on anti-gravity. It’s impossible to put down.",
"Parallel lines have so much in common. It’s a shame they’ll never meet.",
"Why did cowboys hang lanterns on their saddles at night? So they could use saddle-light navigation."
"Why did the bicycle fall over? Because it was two-tired.",
"Why did the chicken join a band? Because it had the drumsticks.",
"Why did the coffee file a police report? It got mugged.",
"Why did the golfer bring two pairs of pants? In case he got a hole in one.",
"Why did the math book look sad? Because it had so many problems.",
"Why did the scarecrow win an award? Because he was outstanding in his field.",
"Why did the tomato turn red? Because it saw the salad dressing.",
"Why don't scientists trust atoms? Because they make up everything.",
"Why don’t scientists trust stairs? Because they’re always up to something.",
"Why don’t skeletons fight each other? They don’t have the guts",
```

Import:
```
curl --verbose http://localhost:8080/jokes/v1/localdb/import \
 --header 'content-type: application/json' \
 --data @dadjokes.json
```
