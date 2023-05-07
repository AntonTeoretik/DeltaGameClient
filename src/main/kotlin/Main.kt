package com.delta

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ktx.app.KtxGame
import ktx.app.KtxScreen


class UI() : KtxGame<KtxScreen>() {

}

class Application(
    private val gameController: GameController,
    private val graphicsComponent: GraphicsComponent
) {

}

/** Launches the desktop (LWJGL3) application. */
fun main(args: Array<String>) {
    val serverAddress = "http://192.168.178.48"
    val playersName = "Anton"

    val gameState = GameState()
    val game1 = GameController(gameState, AppConfig(playersName, serverAddress))

    runBlocking {
        coroutineScope {
            launch {
                game1.start()
            }
        }
    }


//    Lwjgl3Application(UI(), Lwjgl3ApplicationConfiguration().apply {
//        setTitle("Delta!")
//        setWindowedMode(640, 480)
//    })
}