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

    val placeCellHandler = gameController::handlePlaceCellUserRequest
    val finishTurnHandler = gameController::handleFinishTurnRequest
    val shutdownHandler = gameController::shutdown
    val screen = Screen(gameState, gameConfig)

    override fun create() {
        // Set the initial screen to the menu screen
        addScreen(screen)
        setScreen<Screen>()

        val backgroundController = BoardActionsController(
            screen,
            placeCellHandler,
            finishTurnHandler
        )

        // Set up input handling
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(CameraController(screen))
        inputMultiplexer.addProcessor(backgroundController)
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun dispose() {
        shutdownHandler()
        super.dispose()
    }
}