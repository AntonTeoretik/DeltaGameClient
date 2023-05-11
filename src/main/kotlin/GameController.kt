package com.delta

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameController(
    val gameState: GameState = GameState(),
    appConfig: AppConfig = AppConfig()
) {
    val listener = WebSocketListener(appConfig, this::updateGameState)
    val httpClient = ApplicationHttpClient(gameState, appConfig)

    suspend fun start() {
        httpClient.tryLogin()
        coroutineScope {
            launch { listener.start() }
            launch {
                while (gameState.playerID == null) {
                    httpClient.tryAskPlayerId()
                    delay(5000)
                    println("Try ask player id")
                }
                println("PlayerID = ${gameState.playerID}")
            }
        }
    }

    fun handlePlaceCellUserRequest(raw: Int, col: Int): Boolean {
        if (this.gameState.gamePhase != GamePhase.MY_TURN) return false
        return httpClient.askToPlaceCell(raw, col)
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
        } catch (e: Exception) {
            println("Something went wrong: ${e.message}")
        }
    }

    fun shutdown() {
        listener.shutdown()
        httpClient.shutdown()
    }
}

