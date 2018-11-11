package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import fr.zlandorf.asteroids.game.components.TextureComponent
import fr.zlandorf.asteroids.game.components.TiledComponent
import fr.zlandorf.asteroids.game.components.TransformComponent

class RenderingSystem(
    private val camera: Camera,
    private val batch: SpriteBatch
) : BaseEntitySystem(
        Aspect.all(TransformComponent::class.java).one(TextureComponent::class.java, TiledComponent::class.java)
) {

    private lateinit var transformMapper : ComponentMapper<TransformComponent>
    private lateinit var textureMapper : ComponentMapper<TextureComponent>
    private lateinit var tiledMapper : ComponentMapper<TiledComponent>

    override fun processSystem() {
        entityIds
                .data
                .sortedByDescending { transformMapper.get(it).position.z }
                .forEach { process(it) }
    }

    private fun process(entityId: Int) {
        val transform = transformMapper.get(entityId)

        val position = transform.position.cpy()
        val scale = transform.scale
        val rotation = transform.rotation

        val depthFactor = 1f / position.z
        position.add(
                (position.x - camera.position.x) * depthFactor,
                (position.y - camera.position.y) * depthFactor,
                0f
        )

        if (textureMapper.has(entityId)) {
            // FIXME: why is the parallax not scrolling at the same speed ?!


            textureMapper.get(entityId).texture?.let { texture ->
                val width = texture.regionWidth.toFloat()
                val height = texture.regionHeight.toFloat()

                val originX = width / 2f
                val originY = height / 2f

                batch.draw(
                        texture,
                        position.x - originX, position.y - originY,
                        originX, originY,
                        width, height,
                        scale.x, scale.y,
                        rotation
                )
            }
        } else {
            tiledMapper.get(entityId).tile?.let { tile ->
                val x = camera.position.x - (camera.viewportWidth / 2f) - ((camera.position.x - position.x) % tile.region.regionWidth)
                val y = camera.position.y - (camera.viewportHeight / 2f) - ((camera.position.y - position.y) % tile.region.regionHeight)
                tile.draw(
                        batch,
                        x - tile.region.regionWidth,
                        y - tile.region.regionHeight,
                        camera.viewportWidth + 2f * tile.minWidth,
                        camera.viewportHeight + 2f * tile.minHeight
                )
            }
        }
    }
}

