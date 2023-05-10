package com.delta

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import kotlinx.coroutines.coroutineScope
import ktx.app.KtxGame
import ktx.app.KtxScreen

class GraphicsComponent (
    val gameState: GameState,
    val gameConfig: AppConfig,
    gameController: GameController
) : KtxGame<KtxScreen>()  {

    val placeCellHandler = gameController::handlePlaceCellUserRequest
    val finishTurnHandler = gameController::handleFinishTurnRequest

    override fun create() {
        // Set the initial screen to the menu screen
        val screen = Screen(gameState, gameConfig)
        addScreen(screen)
        setScreen<Screen>()

        val testBackgroundController = TestBackgroundController(
            screen,
            placeCellHandler,
            finishTurnHandler
        )

        // Set up input handling
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(CameraController(screen))
        inputMultiplexer.addProcessor(testBackgroundController)
        Gdx.input.inputProcessor = inputMultiplexer
    }
}