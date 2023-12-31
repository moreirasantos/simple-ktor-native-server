# ktor-native

## Build and Deploy
### Docker
With simply Docker installed, run:
```
docker build -t knative .
docker run -d --name knative -p 8080:8080 knative
docker logs -f knative
```
Dispose of the container by running:
```
docker kill knative
docker rm knative
```
### Your Machine (Linux, Mac, Windows WSL)
Since this is native, your machine needs a few libraries installed to compile and run the app.
1. Curl - libcurl4-openssl-dev
2. Postgres Driver - libpq-dev

You can find an installation example by looking at [Dockerfile](Dockerfile)

With proper gradle version installed, run:
```
docker clean runDebugExecutableNative
```

## TODO
* Need to properly handle a graceful shutdown - which is why I use `docker kill` instead of `docker stop`

## Upcoming Features
1. [PGKN](https://github.com/moreirasantos/pgkn) - Simple PostgreSQL driver agnostic of any ORM
2. [knOOQ](https://github.com/moreirasantos/knooq) - inspired by [jOOQ](https://www.jooq.org/), a much smaller,
less generic DB oriented library for Kotlin Native to make simple Database interactions.


```kotlin
fun main() {
    val driver = PostgresDriver(
        host = "host.docker.internal",
        port = 5432,
        user = "postgres",
        database = "postgres",
        password = "postgres",
    )

    driver.execute("CREATE TABLE my_table(id serial primary key)")
    val list = driver.execute("SELECT * FROM users") {
        mapOf(
            "id" to it.getLong(0),
            "name" to it.getString(1),
            "email" to it.getString(2),
        )
    }
}
```
