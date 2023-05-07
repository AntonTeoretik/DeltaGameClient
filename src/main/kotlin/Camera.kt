package com.delta

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Cursor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Matrix4
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

    fun unproject(screenCoords: Vector3): Vector3 {
        camera.unproject(screenCoords)
        return screenCoords
    }

    fun unprojectScreenCoords(screenX: Int, screenY: Int): Vector3 {
        return camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
    }

    fun updateZoom(cursor: Vector3, amount: Float) {
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
}