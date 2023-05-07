package com.delta

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import java.net.URI
import kotlin.reflect.KFunction1

class WebSocketListener(private val updateFunction: KFunction1<GameLogic, Unit>) {
    suspend fun start() {
        val client = HttpClient(OkHttp) {
            install(WebSockets)
        }

        val serverUri = URI.create("http://192.168.178.48:8081/game") //TODO abstract this

        client.ws(
            method = HttpMethod.Get,
            host = serverUri.host,
            port = serverUri.port,
            path = serverUri.path + "?id=Anton", // TODO abstract this
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
}