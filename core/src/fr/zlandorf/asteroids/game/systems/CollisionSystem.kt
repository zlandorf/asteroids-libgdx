package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.math.Intersector
import fr.zlandorf.asteroids.game.components.BoundsComponent
import fr.zlandorf.asteroids.game.components.TransformComponent
import fr.zlandorf.asteroids.game.domain.CollisionEvent
import fr.zlandorf.asteroids.game.services.TransformService
import net.mostlyoriginal.api.event.common.EventSystem

class CollisionSystem : BaseEntitySystem(
        Aspect.all(TransformComponent::class.java, BoundsComponent::class.java)
) {

    private lateinit var boundsMapper: ComponentMapper<BoundsComponent>
    private lateinit var transformService: TransformService
    private lateinit var eventSystem: EventSystem

    override fun processSystem() {
        val entityIds = getSubscription().entities.let {
            val actives = it.data
            (0 until it.size()).map { i -> actives[i] }
        }
        updateBoundingBoxes(entityIds)
        computeCollisions(entityIds).forEach { eventSystem.dispatch(it) }
    }

    private fun updateBoundingBoxes(entityIds: List<Int>) {
        entityIds.forEach { entityId ->
            val bounds = boundsMapper.get(entityId).bounds
            val transform = transformService.computeRealTransform(entityId)

            bounds.setPosition(transform.position.x, transform.position.y)
            bounds.setScale(transform.scale.x, transform.scale.y)
            bounds.rotation = transform.rotation
        }
    }

    private fun computeCollisions(entityIds: List<Int>): Set<CollisionEvent> {
        val collisions = mutableSetOf<CollisionEvent>()
        entityIds.forEach {
            val bounds = boundsMapper.get(it).bounds
            val transform = transformService.computeRealTransform(it)
            entityIds.filter { other -> other != it }.forEach { other ->
                val otherBounds = boundsMapper.get(other).bounds
                val otherTransform = transformService.computeRealTransform(other)

                if (transform.position.z == otherTransform.position.z && Intersector.overlapConvexPolygons(bounds, otherBounds)) {
                    collisions.add(CollisionEvent(it, other))
                }
            }
        }
        return collisions
    }
}