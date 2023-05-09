package com.delta

import com.badlogic.gdx.graphics.Color

class DynamicColor(color: Color, val duration: Float) {
    var timer = 0.0f
    var animationInProgress = false
    var currentColor = color.cpy()
    var nextColor = Color.BLACK.cpy()

    fun setNewColor(newColor: Color) {
        currentColor.set(getColor())
        nextColor.set(newColor)
        timer = 0.0f
        animationInProgress = true
    }

    fun getColor(): Color {
        if (animationInProgress) {
            val t = timer / duration
            return currentColor.cpy().lerp(nextColor, t)
        }
        return currentColor.cpy()
    }

    fun update(time: Float) {
        if (animationInProgress) {
            timer += time

            if (timer > duration) {
                animationInProgress = false
                println("Animation ends")
                timer = 0.0f
                currentColor.set(nextColor)
            }
        }
    }
}