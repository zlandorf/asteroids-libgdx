package fr.zlandorf.asteroids.game.domain

import com.badlogic.gdx.math.Vector3

data class Transform(
        var position: Vector3 = Vector3(0f, 0f, 0f),
        val scale: Vector3 = Vector3(1f, 1f, 1f),
        var rotation: Float = 0f
) {
    fun cpy() = Transform(position.cpy(), scale.cpy(), rotation)
}