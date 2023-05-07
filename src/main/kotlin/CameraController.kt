package com.delta

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector3

class CameraController(private val screen: Screen) : InputAdapter() {
    private var lastPosition = Vector3()

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        lastPosition.set(screen.camera.unprojectScreenCoords(screenX, screenY))
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val now = screen.camera.screenToWorld(screenX, screenY)
        lastPosition.sub(now)
        screen.camera.translate(lastPosition)
        lastPosition.set(screen.camera.screenToWorld(screenX, screenY))
        return true
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        val cursor = screen.camera.screenToWorld(Gdx.input.x, Gdx.input.y)
        screen.camera.updateZoom(cursor, amountY * 0.05f)
        return true
    }
}