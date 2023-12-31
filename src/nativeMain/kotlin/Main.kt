import io.github.moreirasantos.pgkn.PostgresDriver
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import platform.posix.exit
import platform.posix.getenv
import service.PokemonService
import service.UserService

var job: CompletableJob? = null


@OptIn(ExperimentalForeignApi::class)
fun main() {
    runBlocking {
        val driver = PostgresDriver(
            host = getenv("PG_HOST")?.toKString() ?: "localhost",
            port = getenv("PG_PORT")?.toKString()?.takeIf { it.isNotEmpty() }?.toInt() ?: 5432,
            user = getenv("PG_USER")?.toKString() ?: "postgres",
            database = getenv("PG_DATABASE")?.toKString() ?: "postgres",
            password = getenv("PG_PASSWORD")?.toKString() ?: "postgres",
        )

        embeddedServer(
            factory = CIO,
            port = 8080,
            host = "0.0.0.0",
            module = { module(UserService(driver), PokemonService()) },
            parentCoroutineContext = this.coroutineContext
        ).apply {
            job = stopServerOnCancellation()
            start(wait = true)
        }

    }
}

fun Application.module(userService: UserService, pokemonService: PokemonService) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/users") {
//            call.respond(userService.list())
            call.respond(userService.knooqList())
        }
        get("/pokemon") {
            call.respond(pokemonService.ditto())
        }

        get("/shutdown") {
            println("shutting down...")
            call.respondText("shutting down...")
            job?.cancelAndJoin()
            exit(0)
            println("This should not happen")
        }
    }
}