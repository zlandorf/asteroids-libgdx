package fr.zlandorf.asteroids.game.services

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.math.Vector3
import fr.zlandorf.asteroids.game.components.ParentComponent
import fr.zlandorf.asteroids.game.components.TransformComponent
import fr.zlandorf.asteroids.game.domain.Transform

class TransformService : BaseSystem() {

    private lateinit var transformMapper : ComponentMapper<TransformComponent>
    private lateinit var parentMapper : ComponentMapper<ParentComponent>

    override fun processSystem() {
    }

    fun computeRealTransform(entityId: Int): Transform {
        val transform = transformMapper.get(entityId).transform
        val parent = if (parentMapper.has(entityId)) parentMapper.get(entityId).parent else null

        if (parent != null && transformMapper.has(parent)) {
            val parentTransform = computeRealTransform(parent)

            return Transform(
                    transform.position.cpy().rotate(Vector3.Z, parentTransform.rotation).add(parentTransform.position),
                    transform.scale.cpy().scl(parentTransform.scale),
                    transform.rotation + parentTransform.rotation
            )
        }
        return transform.cpy()
    }

}