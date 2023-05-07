package com.delta

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.app.KtxScreen


class Screen() : KtxScreen {
    private var mapPosition = Vector3(0.0f, 0.0f, 0.0f)

    private val viewport = FitViewport(10f * Gdx.graphics.width / Gdx.graphics.height, 10f)
    private val uiViewport = ScreenViewport()

    val camera = Camera()

    private var text = ""

    // Create the frame buffer and blur shader program

    override fun show() {
        camera.setToOrtho(viewport)
    }

    override fun resize(width: Int, height: Int) {
        uiViewport.update(width, height, true)

        viewport.setWorldSize(10f * width / height, 10f)
        // Update the viewport dimensions
        viewport.update(width, height, true)

        // Update the camera and projection matrix to match the viewport
        camera.setToOrtho(viewport)
        camera.updatePosition(mapPosition)
        // Update the camera
        camera.update()
    }

    override fun render(delta: Float) {
        viewport.apply()
        // Set the viewport to the correct size and position
        camera.update()
        mapPosition.set(camera.getPosition())

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        uiViewport.apply()
        //Display text
        drawTextTopLeft(batch, BitmapFont(), text)
    }

    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()

    private fun drawHexagon(polygon: Polygon, color: Color) {
        val vertices = FloatArray(polygon.transformedVertices.size)
        polygon.vertices.forEachIndexed { index, value ->
            vertices[index] = value
        }


        shapeRenderer.projectionMatrix = camera.projMatrix()

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = color

        for (i in 2 until vertices.size - 2 step 2) {
            shapeRenderer.triangle(
                vertices[0], vertices[1],
                vertices[i], vertices[i + 1],
                vertices[i + 2], vertices[i + 3]
            )
        }

        shapeRenderer.end()

    }
    private fun drawCartesianGrid(numCellsX: Int, numCellsY: Int, cellSize: Float, color: Color) {
        shapeRenderer.projectionMatrix = camera.projMatrix()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = color

        // Draw vertical lines
        for (i in 0..numCellsX) {
            val x = i * cellSize - numCellsX / 2f * cellSize
            shapeRenderer.line(x, -numCellsY / 2f * cellSize, x, numCellsY / 2f * cellSize)
        }

        // Draw horizontal lines
        for (i in 0..numCellsY) {
            val y = i * cellSize - numCellsY / 2f * cellSize
            shapeRenderer.line(-numCellsX / 2f * cellSize, y, numCellsX / 2f * cellSize, y)
        }

        shapeRenderer.end()
    }

    private fun drawTextTopLeft(batch: SpriteBatch, font: BitmapFont, text: String) {
        batch.projectionMatrix = uiViewport.camera.combined
        batch.begin()
        font.color = Color.YELLOW
        font.draw(batch, text, 10f, 30f)
        batch.end()
    }

    override fun dispose() {
    }
}