package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.managers.GroupManager
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import fr.zlandorf.asteroids.game.AsteroidsGame.Companion.assets
import fr.zlandorf.asteroids.game.Groups
import fr.zlandorf.asteroids.game.components.*
import fr.zlandorf.asteroids.game.domain.Transform
import fr.zlandorf.asteroids.game.services.polygon

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
            val texture = assets.bullet
            val scale = Vector3(0.5f, 0.5f, 1f)

            val bullet = world.edit(world.create())
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
                            velocity = heading.scl(gun.bulletSpeed),
                            maxSpeed = gun.bulletSpeed
                    ))
                    .add(BoundsComponent(
                            bounds = texture.polygon()
                    ))
                    .entity

            world.getSystem(GroupManager::class.java).add(bullet, Groups.BULLETS)

            gun.timeSinceLastShot = 0f
        }
    }

}