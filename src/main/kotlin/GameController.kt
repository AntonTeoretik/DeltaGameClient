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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GameController(
    val appConfig: AppConfig = AppConfig()
) {
    data class Player(val id: String, val pwd: String)
    var gameStarted: Boolean = false

    var player: Player? = null
    var playerID: PlayerID? = null
    private var gameState: GameLogic? = null

    val fullServerAddress = "${appConfig.serverAddress}:${AppConfig.httpPort}"

    val listener = WebSocketListener(
        AppConfig(),
        this::updateGameState
    )

    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            gson()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    fun start() {
        runBlocking {
            tryLogin()
            coroutineScope {
                launch { listener.start() }
                launch {
                    tryAskPlayerId()
                }
            }
        }
    }

    fun tryLogin() {
        runBlocking {
            val response: HttpResponse = client.post {
                url("$fullServerAddress/login")
                parameter("id", appConfig.playersName)
                parameter("server_pwd", "Delta!!!")
            }

            if (response.status.isSuccess()) {
                player = Gson().fromJson(response.body<String>(), Player::class.java)
            } else {
                println(response.body<String>())
            }
        }
    }

    fun tryAskPlayerId() {
        runBlocking {
            val response: HttpResponse = client.get {
                url("$fullServerAddress/playerID")
                parameter("id", player?.id)
                parameter("pwd", player?.pwd)
            }
            if (response.status.isSuccess()) {
                playerID = Gson().fromJson(response.body<String>(), PlayerID::class.java)
                gameStarted = true
            } else {
                println(response.body<String>())
            }
        }
    }

    fun askToPlaceCell(raw: Int, col: Int) {
        runBlocking {
            val response: HttpResponse = client.post {
                url("$fullServerAddress/placeCell")
                parameter("id", player?.id)
                parameter("pwd", player?.pwd)
                parameter("raw", raw.toString())
                parameter("col", col.toString())
            }

            if (!response.status.isSuccess()) {
                println(response.body<String>())
            }
        }
    }

    fun askToEndTurn() {
        runBlocking {
            val response: HttpResponse = client.post {
                url("$fullServerAddress/placeCell")
                parameter("id", player?.id)
                parameter("pwd", player?.pwd)
            }

            if (!response.status.isSuccess()) {
                println(response.body<String>())
            }
        }
    }

    fun updateGameState(gameState: GameLogic) {
        this.gameState = gameState
        if (!gameStarted) {
            tryAskPlayerId()
        }
    }
}