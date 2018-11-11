package fr.zlandorf.asteroids.game.utils

import com.badlogic.gdx.math.Vector2

fun Vector2.truncate(max: Float) {
    if (len2() > max * max) {
        nor().scl(max, max)
    }
}

fun Vector2.perpendicular() = Vector2(y, -x)
