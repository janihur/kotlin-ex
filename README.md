# kotlin-ex

Random Kotlin examples and code snippets for learning purposes.

## jokes

An example Kotlin Spring Boot API application with H2 database backend.

Originally https://kotlinlang.org/docs/jvm-get-started-spring-boot.html

Initial application skeleton created by: https://start.spring.io/

Run the application in the IDE:
```
Go to file: JokesApplication.kt
 > Ctrl + Shift + F10
```

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

curl --verbose http://localhost:8080/jokes/v1/6
```

## snippets

Random Kotlin snippets.
