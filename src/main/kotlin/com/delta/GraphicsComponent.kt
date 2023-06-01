package com.delta

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import ktx.app.KtxGame
import ktx.app.KtxScreen

class GraphicsComponent(
    private val gameState: GameState,
    private val gameConfig: AppConfig,
    private val gameController: GameController
) : KtxGame<KtxScreen>() {

    val shutdownHandler = gameController::shutdown
    private val finishTurnHandler = gameController::handleFinishTurnRequest
    private val placeCellHandler = gameController::handlePlaceCellUserRequest

    override fun create() {
        val screen = Screen(gameState, gameConfig)


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