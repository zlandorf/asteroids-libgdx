package fr.zlandorf.asteroids.game.domain

import net.mostlyoriginal.api.event.common.Event

data class Collision(val entityAId: Int, val entityBId: Int) : Event {
    override fun equals(other: Any?): Boolean {
        if (other != null && other is Collision) {
            return (entityAId == other.entityAId && entityBId == other.entityBId)
                    || (entityAId == other.entityBId && entityBId == other.entityAId)
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return entityAId.hashCode() + entityBId.hashCode()
    }
}