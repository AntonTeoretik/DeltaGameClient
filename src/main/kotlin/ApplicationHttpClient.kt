package com.delta

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.runBlocking

class ApplicationHttpClient(
    val gameState: GameState,
    val appConfig: AppConfig = AppConfig()
) {
    val fullServerAddress = "${appConfig.serverAddress}:${AppConfig.httpPort}"

    data class Player(val id: String, val pwd: String)
    var player: Player? = null

    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            gson()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    fun tryLogin() : Boolean {
        return runBlocking {
            val response: HttpResponse = client.post {
                url("$fullServerAddress/login")
                parameter("id", appConfig.playersName)
                parameter("server_pwd", "Delta!!!")
            }

            if (response.status.isSuccess()) {
                player = Gson().fromJson(response.body<String>(), Player::class.java)
                return@runBlocking true
            } else {
                println(response.body<String>())
                return@runBlocking false
            }
        }
    }

    fun tryAskPlayerId() : Boolean {
        return runBlocking {
            val response: HttpResponse = client.get {
                url("$fullServerAddress/playerID")
                parameter("id", player?.id)
                parameter("pwd", player?.pwd)
            }
            if (response.status.isSuccess()) {
                gameState.playerID = Gson().fromJson(response.body<String>(), PlayerID::class.java)
                return@runBlocking true
            } else {
                println(response.body<String>())
                return@runBlocking false
            }
        }
    }

    fun askToPlaceCell(raw: Int, col: Int) : Boolean {
        return runBlocking {
            val response: HttpResponse = client.post {
                url("$fullServerAddress/placeCell")
                parameter("id", player?.id)
                parameter("pwd", player?.pwd)
                parameter("raw", raw.toString())
                parameter("col", col.toString())
            }

            if (!response.status.isSuccess()) {
                println(response.body<String>())
                return@runBlocking false
            }
            return@runBlocking true
        }
    }

    fun askToEndTurn() : Boolean {
        return runBlocking {
            val response: HttpResponse = client.post {
                url("$fullServerAddress/placeCell")
                parameter("id", player?.id)
                parameter("pwd", player?.pwd)
            }

            if (!response.status.isSuccess()) {
                println(response.body<String>())
                return@runBlocking false
            }
            return@runBlocking true
        }
    }

}