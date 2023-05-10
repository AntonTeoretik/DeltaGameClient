package com.delta

import com.badlogic.gdx.InputAdapter
import java.awt.event.KeyEvent

class TestBackgroundController(
    private val screen: Screen,
    private val placeCellHandler: (raw: Int, col: Int) -> Boolean,
    private val finishTurnHandler: () -> Boolean,

    ) : InputAdapter() {
    var state = 0
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val point = screen.camera.screenToWorld2D(screenX, screenY)
        println("${point.x} ${point.y}")
        screen.cells?.forEach {
            if (it.contains(point)) {
                placeCellHandler(it.raw, it.col)
            }
        }
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == KeyEvent.VK_ENTER) {
            finishTurnHandler()
        }
        return true
    }

}