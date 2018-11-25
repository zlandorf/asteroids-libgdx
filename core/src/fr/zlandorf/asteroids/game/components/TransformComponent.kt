package fr.zlandorf.asteroids.game.components

import com.artemis.Component
import fr.zlandorf.asteroids.game.domain.Transform

data class TransformComponent(
        val transform: Transform = Transform()
) : Component()