package fr.zlandorf.asteroids.game.components

import com.artemis.Component

data class GunComponent(
        var coolDown: Float = 1f,
        var projectileSpeed: Float = 1000f
) : Component() {
    var timeSinceLastShot: Float = 0f
}