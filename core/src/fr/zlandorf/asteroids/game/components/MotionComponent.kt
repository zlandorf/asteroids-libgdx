package fr.zlandorf.asteroids.game.components

import com.artemis.Component
import com.badlogic.gdx.math.Vector2

data class MotionComponent(
        var velocity: Vector2 = Vector2(),
        var acceleration: Vector2 = Vector2(),
        var maxSpeed: Float = Float.MAX_VALUE,
        var angularVelocity: Float = 0f,
        var angularAcceleration: Float = 0f,
        var maxAngularSpeed: Float = 5f
) : Component()