package com.delta

import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2

class Cell(val raw: Int, val col: Int) {
    val color = DynamicColor(ColorSettings.Empty, 0.3f)

    val cellSize: Float = 1.0f
    val zoom: Float = 0.9f

    val polygon = createCellPolygon(zoom)
    val smallPolygon = createCellPolygon(0.2f)
    val mediumPolygon = createCellPolygon(0.7f)

    fun contains(point: Vector2): Boolean {
        return polygon.contains(point)
    }

    private fun createCellPolygon(zoom : Float): Polygon {
        val xCenter = raw.toFloat() * cellSize
        val yCenter = col.toFloat() * cellSize

        val xOffset = zoom * (cellSize / 2)
        val yOffset = zoom * (cellSize / 2)

        return Polygon(
            floatArrayOf(
                xCenter + xOffset, yCenter + yOffset,
                xCenter - xOffset, yCenter + yOffset,
                xCenter - xOffset, yCenter - yOffset,
                xCenter + xOffset, yCenter - yOffset,
            )
        )
    }
}