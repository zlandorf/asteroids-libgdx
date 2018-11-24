package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import fr.zlandorf.asteroids.game.components.ParentComponent
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
    private lateinit var parentMapper : ComponentMapper<ParentComponent>

    private val renderList = mutableListOf<Int>()

    override fun inserted(entityId: Int) {
        super.inserted(entityId)
        renderList.apply {
            add(entityId)
            sortWith(Comparator { entity1, entity2 ->
                val z1 = computeRealPosition(entity1).z
                val z2 = computeRealPosition(entity2).z
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
        val textureComponent = textureMapper.get(entityId)
        val texture = textureComponent.texture

        if (!textureComponent.visible || texture == null) {
            return
        }

        val position = computeRealPosition(entityId)
        val scale = computeRealScale(entityId)
        val rotation = computeRealRotation(entityId)

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

    private fun computeRealPosition(entityId: Int): Vector3 {
        val position = transformMapper.get(entityId).position
        val parent = if (parentMapper.has(entityId)) parentMapper.get(entityId).parent else null

        if (parent != null && transformMapper.has(parent)) {
            val parentPosition = computeRealPosition(parent)
            val parentRotation = computeRealRotation(parent)
            return position.cpy()
                    .rotate(Vector3.Z, parentRotation)
                    .add(parentPosition)
        }
        return position.cpy()
    }

    private fun computeRealRotation(entityId: Int): Float {
        val rotation = transformMapper.get(entityId).rotation
        val parent = if (parentMapper.has(entityId)) parentMapper.get(entityId).parent else null

        if (parent != null && transformMapper.has(parent)) {
            val parentRotation = computeRealRotation(parent)
            return parentRotation + rotation
        }

        return rotation
    }

    private fun computeRealScale(entityId: Int): Vector2 {
        val scale = transformMapper.get(entityId).scale
        val parent = if (parentMapper.has(entityId)) parentMapper.get(entityId).parent else null

        if (parent != null && transformMapper.has(parent)) {
            val parentScale = computeRealScale(parent)
            return parentScale.cpy().scl(scale)
        }

        return scale.cpy()
    }
}

