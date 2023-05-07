package com.delta

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import ktx.app.KtxGame
import ktx.app.KtxScreen

class GraphicsComponent (
    gameState: GameState,
    gameConfig: AppConfig,
    gameController: GameController
) : KtxGame<KtxScreen>()  {
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