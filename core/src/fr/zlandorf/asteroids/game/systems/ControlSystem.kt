package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import fr.zlandorf.asteroids.game.Tags
import fr.zlandorf.asteroids.game.components.MotionComponent
import fr.zlandorf.asteroids.game.components.ControlComponent
import fr.zlandorf.asteroids.game.components.TextureComponent
import fr.zlandorf.asteroids.game.components.TransformComponent

class ControlSystem : IteratingSystem(
        Aspect.all(ControlComponent::class.java, MotionComponent::class.java, TransformComponent::class.java)
) {

    private lateinit var controlMapper: ComponentMapper<ControlComponent>
    private lateinit var motionMapper: ComponentMapper<MotionComponent>
    private lateinit var transformMapper: ComponentMapper<TransformComponent>
    private lateinit var tagManager: TagManager

    private val thruster = Vector2()

    override fun process(entityId: Int) {
        moveShip(entityId)
        displayBlips()
    }

    private fun moveShip(entityId: Int) {
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

    private fun displayBlips() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            Tags.FORWARD_LEFT_THRUSTER_BLIP.show()
            Tags.FORWARD_RIGHT_THRUSTER_BLIP.show()
        } else {
            Tags.FORWARD_LEFT_THRUSTER_BLIP.hide()
            Tags.FORWARD_RIGHT_THRUSTER_BLIP.hide()
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !Gdx.input.isKeyPressed(Input.Keys.UP)) {
            Tags.BACKWARD_LEFT_THRUSTER_BLIP.show()
            Tags.BACKWARD_RIGHT_THRUSTER_BLIP.show()
        } else {
            Tags.BACKWARD_LEFT_THRUSTER_BLIP.hide()
            Tags.BACKWARD_RIGHT_THRUSTER_BLIP.hide()
        }

        // Turn
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            Tags.TURN_LEFT_BLIP.show()
        } else {
            Tags.TURN_LEFT_BLIP.hide()
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            Tags.TURN_RIGHT_BLIP.show()
        } else {
            Tags.TURN_RIGHT_BLIP.hide()
        }
    }

    private fun String.show() {
        textureComponent().visible = true
    }

    private fun String.hide() {
        textureComponent().visible = false
    }

    private fun String.textureComponent() = tagManager.getEntity(this).getComponent(TextureComponent::class.java)

}