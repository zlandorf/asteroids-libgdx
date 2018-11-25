package fr.zlandorf.asteroids.game.services

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Polygon

fun TextureRegion.polygon() = Polygon(floatArrayOf(
        - regionWidth.toFloat() / 2f, - regionHeight.toFloat() / 2f,
        regionWidth.toFloat() / 2f, - regionHeight.toFloat() / 2f,
        regionWidth.toFloat() / 2f, regionHeight.toFloat() / 2f,
        - regionWidth.toFloat() / 2f, regionHeight.toFloat() / 2f
)).apply {
    setOrigin(0f, 0f)
}