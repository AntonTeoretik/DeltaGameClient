package com.delta

import com.badlogic.gdx.InputAdapter

class TestBackgroundController(private val screen: Screen) : InputAdapter() {
    var state = 0
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        state = (state + 1) % 4
        println(state)

        when (state) {
            0 -> screen.backgroundColor.setNewColor(ColorSettings.Player1)
            1 -> screen.backgroundColor.setNewColor(ColorSettings.Player2)
            2 -> screen.backgroundColor.setNewColor(ColorSettings.Player3)
            else -> screen.backgroundColor.setNewColor(ColorSettings.Player4)
        }

        return true
    }
}