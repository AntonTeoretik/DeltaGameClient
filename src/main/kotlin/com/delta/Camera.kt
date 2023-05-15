package com.delta

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport

class Camera() {
    private val camera = OrthographicCamera()

    fun setToOrtho(viewport: Viewport) {
        camera.setToOrtho(true, viewport.worldWidth, viewport.worldHeight)
    }

    fun updatePosition(position: Vector3) {
        camera.position.set(position)
    }

    fun update() {
        camera.update()
    }

    fun projMatrix(): Matrix4? {
        return camera.combined
    }

    fun getPosition(): Vector3 = camera.position

    fun translate(position: Vector3) {
        camera.translate(position)
        camera.update()
    }

    fun unprojectScreenCoords(screenX: Int, screenY: Int): Vector3 {
        return camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
    }

    fun updateZoom(cursor: Vector3, amount: Float) {
        if (camera.zoom < 0.1 &&
            amount < 0 ||
            camera.zoom > 10 &&
            amount > 0
        ) {
            return
        }

        val prevZoom = camera.zoom
        camera.zoom = camera.zoom + amount

        val newZoom = camera.zoom
        val zoomFactor = newZoom / prevZoom
        camera.position.scl(zoomFactor)
        cursor.scl(zoomFactor)
        camera.position.sub(cursor.x - cursor.x / zoomFactor, cursor.y - cursor.y / zoomFactor, 0f)
        camera.update()
    }

    fun screenToWorld(screenX: Int, screenY: Int): Vector3 {
        val vec = Vector3(screenX.toFloat(), screenY.toFloat(), 0f)
        return camera.unproject(vec)
    }

    fun screenToWorld2D(screenX: Int, screenY: Int): Vector2 {
        val vec = Vector3(screenX.toFloat(), screenY.toFloat(), 0f)
        camera.unproject(vec)
        return Vector2(vec.x, vec.y)
    }

}