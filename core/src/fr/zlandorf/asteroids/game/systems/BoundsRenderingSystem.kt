package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import fr.zlandorf.asteroids.game.components.BoundsComponent
import fr.zlandorf.asteroids.game.components.TransformComponent
import fr.zlandorf.asteroids.game.services.TransformService

class BoundsRenderingSystem(
    private val camera: Camera,
    private val batch: SpriteBatch,
    private val renderer: ShapeRenderer = ShapeRenderer()
) : IteratingSystem(
        Aspect.all(TransformComponent::class.java, BoundsComponent::class.java)
) {

    private lateinit var transformService: TransformService
    private lateinit var boundsMapper: ComponentMapper<BoundsComponent>

    override fun begin() {
        super.begin()
        batch.end()
    }

    override fun end() {
        super.end()
        batch.begin()
    }

    override fun process(entityId: Int) {
        val bounds = boundsMapper.get(entityId).bounds
        val transform = transformService.computeRealTransform(entityId)
        val position = transform.position
        val scale = transform.scale
        val rotation = transform.rotation

        val depthFactor = (100f - position.z) / 100f
        position.set(
                camera.position.x - (camera.position.x - position.x) * depthFactor,
                camera.position.y - (camera.position.y - position.y) * depthFactor,
                0f
        )

        val width = bounds.width
        val height = bounds.height
        val origin = Vector2(width / 2f, height / 2f)

        renderer.projectionMatrix = batch.projectionMatrix
        renderer.transformMatrix = batch.transformMatrix
        renderer.translate(position.x, position.y, 0f)
        renderer.rotate(0f, 0f, 1f, rotation)

        renderer.begin(ShapeRenderer.ShapeType.Line)
        renderer.color = Color.BLUE
        renderer.rect(
                - origin.x, - origin.y,
                origin.x, origin.y,
                width, height,
                scale.x * depthFactor, scale.y * depthFactor,
                rotation
        )
        renderer.end()
    }

}

