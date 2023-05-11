package com.delta

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import java.net.URI
import kotlin.reflect.KFunction1

class WebSocketListener(
    private val appConfig: AppConfig,
    private val updateFunction: KFunction1<GameLogic, Unit>
) {
    private val client = HttpClient(OkHttp) {
        install(WebSockets)
    }

    suspend fun start() {
        val serverUri = URI.create("${appConfig.serverAddress}:${AppConfig.webSocketPort}/game")
        client.ws(
            method = HttpMethod.Get,
            host = serverUri.host,
            port = serverUri.port,
            path = serverUri.path + "?id=${appConfig.playersName}",
        ) {
            try {
                // Connection succeeded
                println("Connected to WebSocket server")

                // Read messages from the server
                for (message in incoming) {
                    if (message is Frame.Text) {
                        val text = message.readText()
                        println("Received message: $text")
                        try {
                            val gameState = Gson().fromJson(text, GameLogic::class.java)
                            updateFunction(gameState)
                        } catch (e : Exception) {
                            println("Failed to convert message to game state")
                        }
                    }
                }

            } finally {

            }
        }

        client.close()
    }

    fun shutdown() {
        client.close()
    }
}