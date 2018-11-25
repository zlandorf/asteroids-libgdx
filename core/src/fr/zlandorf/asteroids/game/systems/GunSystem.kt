package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import fr.zlandorf.asteroids.game.AsteroidsGame.Companion.assets
import fr.zlandorf.asteroids.game.components.GunComponent
import fr.zlandorf.asteroids.game.components.MotionComponent
import fr.zlandorf.asteroids.game.components.TextureComponent
import fr.zlandorf.asteroids.game.components.TransformComponent

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
            val transformComponent = transformMapper.get(entityId)

            val heading = Vector2.Y.cpy().rotate(transformComponent.rotation).nor()
            val gunOffset = heading.cpy().scl(40f)

            world.edit(world.create())
                    .add(TextureComponent(
                            texture = assets.projectile,
                            layer = 3
                    ))
                    .add(TransformComponent(
                            position = transformComponent.position.cpy().add(gunOffset.x, gunOffset.y, 0f),
                            rotation = transformComponent.rotation,
                            scale = Vector2(0.5f, 0.5f)
                    ))
                    .add(MotionComponent(
                            velocity = heading.scl(gun.projectileSpeed),
                            maxSpeed = gun.projectileSpeed
                    ))

            gun.timeSinceLastShot = 0f
        }
    }

}