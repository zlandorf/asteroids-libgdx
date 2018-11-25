package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import fr.zlandorf.asteroids.game.AsteroidsGame.Companion.assets
import fr.zlandorf.asteroids.game.components.*
import fr.zlandorf.asteroids.game.domain.Transform

class GunSystem : IteratingSystem(
        Aspect.all(GunComponent::class.java, TransformComponent::class.java)
) {

    private lateinit var gunMapper: ComponentMapper<GunComponent>
    private lateinit var transformMapper: ComponentMapper<TransformComponent>

    override fun process(entityId: Int) {
        val gun = gunMapper.get(entityId)

        gun.timeSinceLastShot += world.delta

        // FIRE !
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && gun.timeSinceLastShot > gun.coolDown) {
            val transform = transformMapper.get(entityId).transform

            val heading = Vector2.Y.cpy().rotate(transform.rotation).nor()
            val gunOffset = heading.cpy().scl(40f)
            val position = transform.position.cpy().add(gunOffset.x, gunOffset.y, 0f)
            val texture = assets.projectile
            val scale = Vector2(0.5f, 0.5f)

            world.edit(world.create())
                    .add(TextureComponent(
                            texture = texture,
                            layer = 3
                    ))
                    .add(TransformComponent(Transform(
                            position = position,
                            rotation = transform.rotation,
                            scale = scale
                    )))
                    .add(MotionComponent(
                            velocity = heading.scl(gun.projectileSpeed),
                            maxSpeed = gun.projectileSpeed
                    ))
                    .add(BoundsComponent(
                            BoundingBox(
                                    Vector3.Zero,
                                    Vector3(
                                            texture.regionWidth.toFloat(),
                                            texture.regionHeight.toFloat(),
                                            0f
                                    )
                            )
                    ))
            gun.timeSinceLastShot = 0f
        }
    }

}