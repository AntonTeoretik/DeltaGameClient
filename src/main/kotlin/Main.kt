package com.delta

import kotlinx.coroutines.*


/** Launches the desktop (LWJGL3) application. */
fun main(args: Array<String>) {
    val serverAddress = "http://192.168.178.48"
    val playersName = "Anton"

    val app1 = Application(AppConfig("A", serverAddress))
    val app2 = Application(AppConfig("B", serverAddress))
    val app3 = Application(AppConfig("C", serverAddress))
    val app4 = Application(AppConfig("D", serverAddress))

    runBlocking {
        coroutineScope {
            launch {
                app1.start()
            }
        }
    }
}