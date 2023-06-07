import app.softwork.sqldelight.postgresdriver.ListenerSupport
import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import platform.posix.exit
import service.PokemonService
import service.UserService

var job: CompletableJob? = null


fun main() {
    runBlocking {
        val driver = PostgresNativeDriver(
            host = "host.docker.internal",
            port = 5432,
            user = "postgres",
            database = "postgres",
            password = "postgres",
            options = null,
            listenerSupport = ListenerSupport.Remote(this)
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
            call.respond(userService.list())
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