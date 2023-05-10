package com.delta

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import kotlinx.coroutines.*
import okhttp3.internal.wait

fun someWork() {
    repeat(1000) {
        println(1)
        Thread.sleep(100)
    }
}

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
            runBlocking { gameController.start() }
        }

        thread.start()

        Lwjgl3Application(graphicsComponent, Lwjgl3ApplicationConfiguration().apply {
            setTitle("Delta!")
            setWindowedMode(640, 480)
        })

        thread.join()
    }
}