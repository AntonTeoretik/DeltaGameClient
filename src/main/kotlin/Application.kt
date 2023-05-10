package com.delta

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import kotlinx.coroutines.*
import okhttp3.internal.wait

class Application(appConfig: AppConfig) {
    private var gameController: GameController
    private var graphicsComponent: GraphicsComponent

    init {
        val gameState = GameState()
        gameController = GameController(gameState, appConfig)
        graphicsComponent = GraphicsComponent(gameState, appConfig, gameController)
    }

    suspend fun start() {
        coroutineScope {
            launch {

                //gameController.start()
                repeat(1000) {
                    println(1)
                    delay(100)
                }
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