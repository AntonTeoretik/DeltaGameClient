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
import kotlin.math.sqrt

class Screen(
    val gameState: GameState,
    gameConfig: AppConfig
) : KtxScreen {
    private var backgroundColorMy: Boolean = false
    var cells: List<Cell>? = null

    private var gameStarted = false
    private var myColor: Color? = null

    // Camera info
    val camera = Camera()
    private var mapPosition = Vector3(0.0f, 0.0f, 0.0f)

    val backgroundColor = DynamicColor(Color.BLACK, 0.1f)

    // viewports
    private val viewport = FitViewport(10f * Gdx.graphics.width / Gdx.graphics.height, 10f)
    private val uiViewport = ScreenViewport()

    // messages
    private var text = gameConfig.playersName

    private fun updateInfo() {
        if (!gameStarted &&
            gameState.gameState != null &&
            gameState.playerID != null
        ) {
            gameStarted = true
            generateCells(gameState.gameState!!.getBoardSize())
            text = gameState.playerID.toString()
        }
        if (gameStarted) {

            myColor = when (gameState.playerID) {
                PlayerID.PLAYER_1 -> ColorSettings.Player1
                PlayerID.PLAYER_2 -> ColorSettings.Player2
                PlayerID.PLAYER_3 -> ColorSettings.Player3
                PlayerID.PLAYER_4 -> ColorSettings.Player4
                null -> null
            }

            if (gameState.gamePhase == GamePhase.MY_TURN &&
                !backgroundColorMy
            ) {
                backgroundColorMy = true
                backgroundColor.setNewColor(myColor!!.cpy().lerp(ColorSettings.Background, 0.6f))
            } else if (
                gameState.gamePhase == GamePhase.WAITING_FOR_OTHERS_TURN &&
                backgroundColorMy
            ) {
                backgroundColorMy = false
                backgroundColor.setNewColor(ColorSettings.Background)
            }

            text = "${gameState.playerID.toString()}\n" +
                    "${gameState.gamePhase}\n" +
                    "${gameState.gameState?.getPlayerResources()}"
        }
    }

    private fun generateCells(size: Int) {
        cells = (0 until size).flatMap { i ->
            (0 until size).map { j ->
                Cell(i, j)
            }
        }
    }

    private fun updateCellsColor(delta: Float) {
        cells!!.forEach {
            val newColor = getCellColor(it.raw, it.col)
            if (!it.color.animationInProgress &&
                it.color.currentColor != newColor
            )
                it.color.setNewColor(newColor)
            it.color.update(delta)
        }
    }

    private fun renderCells() {
        drawBorders()

        cells!!.forEach {
            drawPolygon(it.polygon, it.color.getColor())

            if (gameState.gameState!!.isProductive(it.raw, it.col)) {
                drawPolygon(it.smallPolygon, ColorSettings.Background)
            }
            if (gameState.gameState!!.isBaseCell(it.raw, it.col)) {
                drawPolygon(it.mediumPolygon, it.color.getColor().cpy().lerp(ColorSettings.Background, 0.5f))
            }
        }
    }

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
        updateInfo()
        backgroundColor.update(delta)
        viewport.apply()
        camera.update()
        // Set the viewport to the correct size and position
        mapPosition.set(camera.getPosition())

        // background
        val col = backgroundColor.getColor()
        Gdx.gl.glClearColor(col.r, col.g, col.b, col.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // cells

        if (gameStarted) {
            updateCellsColor(delta)
            renderCells()
        }

        // grid
        // drawCartesianGrid(10, 10, 1f, Color.BLUE)

        // UI
        uiViewport.apply()
        drawTextTopLeft(batch, BitmapFont(), text)
    }

    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()


    private fun getCellColor(raw: Int, col: Int): Color {
        if (
            gameState.gameState == null ||
            gameState.gamePhase == GamePhase.NOT_STARTED
        )
            return ColorSettings.Background

        return when (gameState.gameState!!.getCell(raw, col)) {
            PlayerID.PLAYER_1 -> ColorSettings.Player1
            PlayerID.PLAYER_2 -> ColorSettings.Player2
            PlayerID.PLAYER_3 -> ColorSettings.Player3
            PlayerID.PLAYER_4 -> ColorSettings.Player4
            null -> {
                if (gameState.gamePhase == GamePhase.MY_TURN &&
                    gameState.gameState!!.isValidCellToPlace(raw, col, gameState.playerID!!)
                ) {
                    ColorSettings.Background
                } else {
                    ColorSettings.Empty
                }
            }
        }
    }

    private fun drawPolygon(polygon: Polygon, color: Color) {
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

    private fun drawBorders() {
        shapeRenderer.projectionMatrix = camera.projMatrix()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = ColorSettings.Empty

        val offset = 0.2f
        val size = (sqrt(cells!!.size.toFloat()) - 1) * cells!![0].cellSize

        val x0 = -offset - cells!![0].cellSize / 2
        val x1 = offset + cells!![0].cellSize / 2 + size

        shapeRenderer.rectLine(x0, x0, x1, x0, 0.1f)
        shapeRenderer.rectLine(x0, x0, x0, x1, 0.1f)
        shapeRenderer.rectLine(x1, x0, x1, x1, 0.1f)
        shapeRenderer.rectLine(x0, x1, x1, x1, 0.1f)

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
        font.draw(batch, text, 10f, uiViewport.worldHeight - 30f)
        batch.end()
    }

    override fun dispose() {
    }
}