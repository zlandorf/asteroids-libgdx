package fr.zlandorf.asteroids.game.handlers

import com.artemis.BaseSystem
import fr.zlandorf.asteroids.game.domain.Collision
import net.mostlyoriginal.api.event.common.Subscribe

class CollisionHandler : BaseSystem() {
    override fun processSystem() {
    }

    @Subscribe
    fun handleCollision(event: Collision) {
        println("Coucou ${event.entityAId} ${event.entityBId}")
    }

}