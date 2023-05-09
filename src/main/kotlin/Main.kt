package com.delta

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ktx.app.KtxGame
import ktx.app.KtxScreen


class Application(appConfig: AppConfig) {
    private var gameController : GameController
    private var graphicsComponent : GraphicsComponent

    init {
        val gameState = GameState()
        gameController = GameController(gameState, appConfig)
        graphicsComponent = GraphicsComponent(gameState, appConfig, gameController)
    }

    fun start() {
        runBlocking {
            coroutineScope {
                launch {
                    gameController.start()
                }
                launch {
                    Lwjgl3Application(graphicsComponent, Lwjgl3ApplicationConfiguration().apply {
                        setTitle("Delta!")
                        setWindowedMode(640, 480)
                    })
                }
            }
        }
    }

}

/** Launches the desktop (LWJGL3) application. */
fun main(args: Array<String>) {
    val serverAddress = "http://192.168.178.48"
    val playersName = "Anton"

    val gameState = GameState()
    val game = GameController(gameState, AppConfig(playersName, serverAddress))

    val app = Application(AppConfig(playersName, serverAddress))
    app.start()
}