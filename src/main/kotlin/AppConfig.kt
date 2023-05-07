package com.delta

data class AppConfig(
    val serverAddress: String,
    val httpPort: String = "8080",
    val webSocketPort: String = "8081",
)