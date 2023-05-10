package com.delta

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import java.security.cert.PolicyNode

class Cell(val raw: Int, val col: Int) {
    val color = DynamicColor(ColorSettings.Empty, 0.3f)

    val cellSize: Float = 1.0f
    val zoom: Float = 0.9f

    val polygon = createCellPolygon()

    fun contains(point: Vector2): Boolean {
        return polygon.contains(point)
    }

    fun clickMe() {
        color.setNewColor(Color.WHITE)
    }

    private fun createCellPolygon(): Polygon {
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