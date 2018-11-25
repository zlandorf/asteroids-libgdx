package fr.zlandorf.asteroids.game.components

import com.artemis.Component

data class ControlComponent(
        var thrusterAcceleration: Float = 25f,
        var backThrusterAcceleration: Float = -20f,
        var turnThrusterAcceleration: Float = 5f,
        var gunCooldown: Float = 1f,
        var projectileSpeed: Float = 1000f
) : Component()