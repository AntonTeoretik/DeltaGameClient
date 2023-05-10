package com.delta

import kotlinx.coroutines.*


/** Launches the desktop (LWJGL3) application. */
fun main(args: Array<String>) {
    val serverAddress = "http://192.168.178.48"
    val playersName = args[0]

    val app1 = Application(AppConfig(playersName, serverAddress))

    app1.start()
}