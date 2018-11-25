package fr.zlandorf.asteroids.game.components

import com.artemis.Component
import com.artemis.annotations.EntityId

data class ParentComponent(
        @EntityId val parent: Int? = null
) : Component()