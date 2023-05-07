package com.delta

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GameController(
    val gameState: GameState = GameState(),
    appConfig: AppConfig = AppConfig()
) {
    val listener = WebSocketListener(appConfig, this::updateGameState)
    val httpClient = ApplicationHttpClient(gameState, appConfig)

    fun start() {
        runBlocking {
            httpClient.tryLogin()
            coroutineScope {
                launch { listener.start() }
                launch {
                    httpClient.tryAskPlayerId()
                }
            }
        }
    }

    private fun updateGameState(gameState: GameLogic) {
        this.gameState.gameState = gameState
        if (this.gameState.gamePhase != GamePhase.NOT_STARTED) {
            this.httpClient.tryAskPlayerId()
        }
    }
}