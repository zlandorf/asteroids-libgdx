package fr.zlandorf.asteroids.game.components

import com.artemis.Component
import com.badlogic.gdx.math.Polygon

data class BoundsComponent(
        val bounds: Polygon = Polygon()
) : Component()