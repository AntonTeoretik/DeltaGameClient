package com.delta

class AppConfig(
    val playersName: String = "Anton",
    val serverAddress: String = "http://192.168.178.48",
) {
    companion object {
        val httpPort: String = "8080"
        val webSocketPort: String = "8081"
    }
}