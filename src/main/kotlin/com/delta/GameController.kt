package com.delta

import kotlinx.coroutines.*

class GameController(
    val gameState: GameState = GameState(),
    appConfig: AppConfig = AppConfig()
) {
    val listener = WebSocketListener(appConfig, this::updateGameState)
    val httpClient = ApplicationHttpClient(gameState, appConfig)

    suspend fun start() {
        httpClient.tryLogin()
        coroutineScope {
            launch {
                listener.start()
            }
        }
    }

    fun handlePlaceCellUserRequest(raw: Int, col: Int): Boolean {
        if (this.gameState.gamePhase != GamePhase.MY_TURN) return false

        val response = httpClient.askToPlaceCell(raw, col)
        return response
    }

    fun handleFinishTurnRequest(): Boolean {
        if (this.gameState.gamePhase != GamePhase.MY_TURN) return false
        return httpClient.askToEndTurn()
    }

    private fun updateGameState(gameState: GameLogic) {
        this.gameState.gameState = gameState

        if (this.gameState.gamePhase == GamePhase.NOT_STARTED &&
            this.httpClient.tryAskPlayerId()
        ) {
            this.gameState.gamePhase = GamePhase.WAITING_FOR_OTHERS_TURN
        }

        try {
            this.gameState.gamePhase = GamePhase.WAITING_FOR_OTHERS_TURN
            if (this.gameState.playerID == this.gameState.gameState!!.getCurrentPlayer())
                this.gameState.gamePhase = GamePhase.MY_TURN

            if (this.gameState.gameState!!.isGameOver()) {
                this.gameState.gamePhase = GamePhase.FINISHED
            }

            if (
                this.gameState.gamePhase == GamePhase.MY_TURN &&
                gameState.getPlayerResources()[this.gameState.playerID] == 0
            ) {
                handleFinishTurnRequest()
            }

        } catch (e: Exception) {
            println("Something went wrong: ${e.message}")
        }
    }

    fun shutdown() {
        listener.shutdown()
        httpClient.shutdown()
    }
}

