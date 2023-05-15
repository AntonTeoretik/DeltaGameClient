package com.delta

data class GameState(
    var playerID: PlayerID? = null,
    var gameState: GameLogic? = null,
    var gamePhase: GamePhase = GamePhase.NOT_STARTED
)