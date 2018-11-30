package fr.zlandorf.asteroids.game.entities

import com.artemis.Entity
import com.artemis.World
import com.artemis.managers.GroupManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import fr.zlandorf.asteroids.game.AsteroidsGame
import fr.zlandorf.asteroids.game.Groups
import fr.zlandorf.asteroids.game.components.AnimationComponent
import fr.zlandorf.asteroids.game.components.BoundsComponent
import fr.zlandorf.asteroids.game.components.MotionComponent
import fr.zlandorf.asteroids.game.components.TextureComponent
import fr.zlandorf.asteroids.game.components.TransformComponent
import fr.zlandorf.asteroids.game.domain.Transform
import fr.zlandorf.asteroids.game.services.polygon

fun World.createAsteroid(): Entity? = edit(create())
            .add(TextureComponent())
            .add(AnimationComponent(AsteroidsGame.assets.asteroid))
            .add(TransformComponent(
                    Transform(position= Vector3(-Gdx.graphics.height / 2f, -Gdx.graphics.height / 2f, 0f)
            )))
            .add(MotionComponent(
                    velocity = Vector2(100f, 100f),
                    angularAcceleration = 0f
            ))
            .add(BoundsComponent(
                    bounds = AsteroidsGame.assets.asteroid.keyFrames[0].polygon()
            ))
            .entity?.apply {
                getSystem(GroupManager::class.java).add(this, Groups.ASTEROIDS)
            }
