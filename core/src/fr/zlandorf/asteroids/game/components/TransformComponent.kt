package fr.zlandorf.asteroids.game.components

import com.artemis.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

data class TransformComponent(
    var position: Vector3 = Vector3(0f, 0f, 0f),
    val scale: Vector2 = Vector2(1f, 1f),
    var rotation: Float = 0f
) : Component()