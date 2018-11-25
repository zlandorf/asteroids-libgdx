package fr.zlandorf.asteroids.game.handlers

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.artemis.managers.GroupManager
import com.artemis.managers.TagManager
import fr.zlandorf.asteroids.game.AsteroidsGame.Companion.assets
import fr.zlandorf.asteroids.game.Groups
import fr.zlandorf.asteroids.game.Tags
import fr.zlandorf.asteroids.game.components.AnimationComponent
import fr.zlandorf.asteroids.game.components.TextureComponent
import fr.zlandorf.asteroids.game.components.TransformComponent
import fr.zlandorf.asteroids.game.domain.CollisionEvent
import fr.zlandorf.asteroids.game.systems.AnimationSystem
import net.mostlyoriginal.api.event.common.Subscribe

class CollisionHandler : BaseSystem() {

    private lateinit var tagManager: TagManager
    private lateinit var groupManager: GroupManager
    private lateinit var animationSystem: AnimationSystem

    private lateinit var transformMapper: ComponentMapper<TransformComponent>

    override fun processSystem() {
    }

    @Subscribe
    fun handlePlayerAsteroidCollision(event: CollisionEvent) {
        val spaceship = event.entityAId.spaceShip() ?: event.entityBId.spaceShip()
        val asteroid = event.entityAId.asteroid() ?: event.entityBId.asteroid()

        if (asteroid != null && spaceship != null) {
            // TODO : damage the player's ship
            destroyAsteroid(asteroid)
        }
    }


    @Subscribe
    fun handleBulletAsteroidCollision(event: CollisionEvent) {
        val bullet = event.entityAId.bullet() ?: event.entityBId.bullet()
        val asteroid = event.entityAId.asteroid() ?: event.entityBId.asteroid()

        if (asteroid != null && bullet != null) {
            destroyAsteroid(asteroid)
            world.delete(bullet)
        }
    }

    private fun destroyAsteroid(asteroid: Int) {
        if (transformMapper.has(asteroid)) {
            val transform = transformMapper.get(asteroid).transform
            val explosion = world.edit(world.create())
                    .add(TransformComponent(
                            transform = transform.cpy()
                    ))
                    .add(TextureComponent())
                    .add(AnimationComponent(
                            animation = assets.explosion
                    ))
                    .entity

            animationSystem.deleteAfterAnimation(explosion.id)
        }
        world.delete(asteroid)
    }

    private fun Int.asteroid(): Int? {
        val entity = world.getEntity(this)
        return if (groupManager.isInGroup(entity, Groups.ASTEROIDS)) this else null
    }

    private fun Int.bullet(): Int? {
        val entity = world.getEntity(this)
        return if (groupManager.isInGroup(entity, Groups.BULLETS)) this else null
    }

    private fun Int.spaceShip() = if (tagManager.getEntityId(Tags.SPACE_SHIP) == this) this else null

}