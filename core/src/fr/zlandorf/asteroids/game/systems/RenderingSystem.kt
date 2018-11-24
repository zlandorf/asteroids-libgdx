package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import fr.zlandorf.asteroids.game.components.TextureComponent
import fr.zlandorf.asteroids.game.components.TransformComponent

class RenderingSystem(
    private val camera: Camera,
    private val batch: SpriteBatch
) : BaseEntitySystem(
        Aspect.all(TransformComponent::class.java, TextureComponent::class.java)
) {

    private lateinit var transformMapper : ComponentMapper<TransformComponent>
    private lateinit var textureMapper : ComponentMapper<TextureComponent>

    private val renderList = mutableListOf<Int>()

    override fun inserted(entityId: Int) {
        super.inserted(entityId)
        renderList.apply {
            add(entityId)
            sortWith(Comparator { entity1, entity2 ->
                val z1 = transformMapper.get(entity1).position.z
                val z2 = transformMapper.get(entity2).position.z
                if (z1 == z2) {
                    // if both entities have the same depth, then display them based on their texture layer
                    val layer1 = textureMapper.get(entity1).layer
                    val layer2 = textureMapper.get(entity2).layer
                    - layer1.compareTo(layer2)
                } else {
                    // otherwise sort them based on their depth
                - z1.compareTo(z2)
                }
            })
        }
    }

    override fun removed(entityId: Int) {
        renderList.remove(entityId)
        super.removed(entityId)
    }

    override fun processSystem() {
        renderList.forEach{ process(it) }
    }

    private fun process(entityId: Int) {
        val transform = transformMapper.get(entityId)
        val texture = textureMapper.get(entityId).texture ?: return

        val position = transform.position.cpy()
        val scale = transform.scale
        val rotation = transform.rotation

        val depthFactor = (100f - position.z) / 100f
        position.set(
                camera.position.x - (camera.position.x - position.x) * depthFactor,
                camera.position.y - (camera.position.y - position.y) * depthFactor,
                0f
        )

        val width = texture.regionWidth.toFloat()
        val height = texture.regionHeight.toFloat()

        val originX = width / 2f
        val originY = height / 2f

        batch.draw(
                texture,
                position.x - originX, position.y - originY,
                originX, originY,
                width, height,
                scale.x * depthFactor, scale.y * depthFactor,
                rotation
        )
    }
}

