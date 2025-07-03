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

HTTP calls:
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

## snippets

Random Kotlin snippets.
