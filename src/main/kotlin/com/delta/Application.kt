package com.delta

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import kotlinx.coroutines.*


class Application(appConfig: AppConfig) {
    private var gameController: GameController
    private var graphicsComponent: GraphicsComponent

    init {
        val gameState = GameState()
        gameController = GameController(gameState, appConfig)
        graphicsComponent = GraphicsComponent(gameState, appConfig, gameController)
    }

    fun start() {
        val thread = Thread {
            runBlocking {
                try {
                    gameController.start()
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }

        thread.start()

        Lwjgl3Application(graphicsComponent, Lwjgl3ApplicationConfiguration().apply
        {
            setTitle("Delta!")
            setWindowedMode(640, 480)
        })

        thread.interrupt()
    }
}