package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import fr.zlandorf.asteroids.game.components.CameraTargetComponent
import fr.zlandorf.asteroids.game.components.TransformComponent

class CameraSystem(
        private val camera: Camera,
        private val batch: SpriteBatch
) : IteratingSystem(
        Aspect.all(CameraTargetComponent::class.java, TransformComponent::class.java)
) {
    private lateinit var transformMapper : ComponentMapper<TransformComponent>

    override fun process(entityId: Int) {
        val transform = transformMapper.get(entityId)

        camera.position.set(transform.position)
        camera.update()
        batch.projectionMatrix = camera.combined
    }

}