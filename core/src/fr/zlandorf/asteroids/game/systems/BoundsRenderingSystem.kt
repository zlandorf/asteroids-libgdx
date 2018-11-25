package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polygon
import fr.zlandorf.asteroids.game.components.BoundsComponent
import fr.zlandorf.asteroids.game.components.TransformComponent

class BoundsRenderingSystem(
    private val camera: Camera,
    private val batch: SpriteBatch,
    private val renderer: ShapeRenderer = ShapeRenderer()
) : IteratingSystem(
        Aspect.all(TransformComponent::class.java, BoundsComponent::class.java)
) {

    private lateinit var boundsMapper: ComponentMapper<BoundsComponent>
    private lateinit var transformMapper: ComponentMapper<TransformComponent>

    override fun begin() {
        super.begin()
        batch.end()
    }

    override fun end() {
        super.end()
        batch.begin()
    }

    override fun process(entityId: Int) {
        val depth = transformMapper.get(entityId).transform.position.z
        val polygon = Polygon(boundsMapper.get(entityId).bounds.transformedVertices)

        val depthFactor = (100f - depth) / 100f
        polygon.setPosition(
                camera.position.x - (camera.position.x - polygon.x) * depthFactor,
                camera.position.y - (camera.position.y - polygon.y) * depthFactor
        )
        polygon.setScale(depthFactor, depthFactor)

        renderer.projectionMatrix = batch.projectionMatrix
        renderer.transformMatrix = batch.transformMatrix

        renderer.begin(ShapeRenderer.ShapeType.Line)
        renderer.color = Color.RED
        renderer.polygon(polygon.transformedVertices)
        renderer.end()
    }

}

