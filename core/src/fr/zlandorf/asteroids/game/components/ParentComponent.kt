package fr.zlandorf.asteroids.game.components

import com.artemis.Component

data class ParentComponent(
        val parent: Int? = null
) : Component()