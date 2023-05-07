package com.delta

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Game : KtxGame<KtxScreen>() {
    override fun create() {
        // Set the initial screen to the menu screen
        val screen = Screen()
        addScreen(screen)
        setScreen<Screen>()

        // Set up input handling
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(CameraController(screen))
        Gdx.input.inputProcessor = inputMultiplexer

    }
}

class GraphicsComponent() {

}

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

    val game1 = GameController(serverAddress, "A")
    val game2 = GameController(serverAddress, "B")
    val game3 = GameController(serverAddress, "C")
    val game4 = GameController(serverAddress, "D")


    runBlocking {
        coroutineScope {
            launch {
                game1.start()
            }
            launch {
                game2.start()
            }
            launch {
                game3.start()
            }
            launch {
                game4.start()
            }
        }
    }


//    Lwjgl3Application(UI(), Lwjgl3ApplicationConfiguration().apply {
//        setTitle("Delta!")
//        setWindowedMode(640, 480)
//    })
}