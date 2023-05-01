package com.delta

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import ktx.app.KtxGame
import ktx.app.KtxScreen


class Game() : KtxGame<KtxScreen>()
{

}

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(Game(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Delta!")
        setWindowedMode(640, 480)
    })
}