package com.delta

class Cell(val raw : Int, val col : Int) {
    val color = DynamicColor(ColorSettings.Empty, 0.1f)
}