package service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.curl.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val client = HttpClient(Curl) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}

class PokemonService {
    suspend fun ditto(): Pokemon = client.get("https://pokeapi.co/api/v2/pokemon/ditto").body()
}

@Serializable
data class Pokemon(val id: Int, val name: String, val height: Int, val weight: Int)