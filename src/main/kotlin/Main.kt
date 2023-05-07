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

    val game1 = GameController(AppConfig("A", serverAddress))
    val game2 = GameController(AppConfig("A", serverAddress))
    val game3 = GameController(AppConfig("A", serverAddress))
    val game4 = GameController(AppConfig("A", serverAddress))


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