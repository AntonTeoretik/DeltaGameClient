package com.delta

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import ktx.app.KtxGame
import ktx.app.KtxScreen

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import java.net.URI


interface GameUI {

}

class UI() : KtxGame<KtxScreen>() {

}

class WebController() {
    suspend fun start() {
        val client = HttpClient(OkHttp) {
            install(WebSockets)
        }

        val serverUri = URI.create("http://192.168.178.48:8081/game")

        client.ws(
            method = HttpMethod.Get,
            host = serverUri.host,
            port = serverUri.port,
            path = serverUri.path + "?id=Anton",
        ) {
            try {
                // Connection succeeded
                println("Connected to WebSocket server")

                // Read messages from the server
                for (message in incoming) {
                    if (message is Frame.Text) {
                        val text = message.readText()
                        println("Received message: $text")
                    }
                }

            } finally {

            }
        }

        client.close()
    }
}

class GameController() {
    data class Player(val id: String, val pwd: String)

    val player: Player? = null
    val playerID: PlayerID? = null

    val gameState: GameLogic? = null

    fun start() {}
    fun updateGameState() {}


}

/** Launches the desktop (LWJGL3) application. */
fun main() {

    runBlocking {
        val client = WebController()
        client.start()
    }

//    Lwjgl3Application(UI(), Lwjgl3ApplicationConfiguration().apply {
//        setTitle("Delta!")
//        setWindowedMode(640, 480)
//    })
}