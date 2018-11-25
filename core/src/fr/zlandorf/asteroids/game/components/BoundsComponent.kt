package fr.zlandorf.asteroids.game.components

import com.artemis.Component
import com.badlogic.gdx.math.collision.BoundingBox

data class BoundsComponent(
        val bounds: BoundingBox = BoundingBox()
) : Component()