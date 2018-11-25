package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import fr.zlandorf.asteroids.game.components.BoundsComponent
import fr.zlandorf.asteroids.game.components.TransformComponent
import fr.zlandorf.asteroids.game.domain.Collision
import net.mostlyoriginal.api.event.common.EventSystem

class CollisionSystem : BaseEntitySystem(
        Aspect.all(TransformComponent::class.java, BoundsComponent::class.java)
) {

    private lateinit var boundsMapper: ComponentMapper<BoundsComponent>
    private lateinit var eventSystem: EventSystem

    override fun processSystem() {
        val collisions = mutableSetOf<Collision>()
        val entities = getSubscription().entities.let {
            val actives = it.data
            (0 until it.size()).map { i -> actives[i] }
        }

        entities.forEach {
            val bounds = boundsMapper.get(it).bounds
            entities.filter { other -> other != it }.forEach { other ->
                val otherBounds = boundsMapper.get(other).bounds
                if (bounds.intersects(otherBounds)) {
                    collisions.add(Collision(it, other))
                }
            }
        }

        collisions.forEach { eventSystem.dispatch(it) }
    }
}