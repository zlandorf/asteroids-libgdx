package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import fr.zlandorf.asteroids.game.components.MotionComponent
import fr.zlandorf.asteroids.game.components.TransformComponent
import fr.zlandorf.asteroids.game.domain.Transform
import fr.zlandorf.asteroids.game.services.truncate

class MotionSystem : IteratingSystem(
        Aspect.all(MotionComponent::class.java, TransformComponent::class.java)
) {

    private lateinit var motionMapper: ComponentMapper<MotionComponent>
    private lateinit var transformMapper: ComponentMapper<TransformComponent>

    // Reuse a temporary vector2 to avoid instantiating a new vector2 each update (the scl method modifies the object)
    private val tmp = Vector2()

    override fun process(entityId: Int) {
        val motion = motionMapper.get(entityId)
        val transform = transformMapper.get(entityId).transform

        updateAngularVelocity(motion)
        updateRotation(motion, transform)
        updateVelocity(motion)
        updatePosition(motion, transform)
    }

    private fun updateAngularVelocity(motion: MotionComponent) {
        motion.angularVelocity += motion.angularAcceleration * world.delta
        motion.angularVelocity = Math.max(-motion.maxAngularSpeed, Math.min(motion.maxAngularSpeed, motion.angularVelocity))
    }

    private fun updateRotation(motion: MotionComponent, transform: Transform) {
        transform.rotation += motion.angularVelocity
    }

    private fun updatePosition(motion: MotionComponent, transform: Transform) {
        tmp.set(motion.velocity).scl(world.delta)
        transform.position.add(tmp.x, tmp.y, 0f)
    }

    private fun updateVelocity(motion: MotionComponent) {
        tmp.set(motion.acceleration).scl(world.delta)
        motion.velocity.add(tmp)
        motion.velocity.truncate(motion.maxSpeed)
    }

}