package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import fr.zlandorf.asteroids.game.components.SpaceshipControlComponent
import fr.zlandorf.asteroids.game.components.MotionComponent
import fr.zlandorf.asteroids.game.components.TransformComponent

class SpaceshipControlSystem : IteratingSystem(
        Aspect.all(SpaceshipControlComponent::class.java, MotionComponent::class.java, TransformComponent::class.java)
) {

    private lateinit var controlMapper: ComponentMapper<SpaceshipControlComponent>
    private lateinit var motionMapper: ComponentMapper<MotionComponent>
    private lateinit var transformMapper: ComponentMapper<TransformComponent>

    private val thruster = Vector2()

    override fun process(entityId: Int) {
        val control = controlMapper.get(entityId)

        var angularThruster = 0f
        thruster.set(0f, 0f)

        // Forward and back
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            thruster.add(0f, control.thrusterAcceleration)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            thruster.add(0f, control.backThrusterAcceleration)
        }

        // Turn
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angularThruster += control.turnThrusterAcceleration
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            angularThruster -= control.turnThrusterAcceleration
        }

        thruster.rotate(transformMapper.get(entityId).rotation)
        motionMapper.get(entityId).apply {
            acceleration.set(thruster)
            angularAcceleration = angularThruster
        }
    }

}