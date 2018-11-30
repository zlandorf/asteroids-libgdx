package fr.zlandorf.asteroids.game.entities

import com.artemis.Entity
import com.artemis.World
import com.artemis.managers.GroupManager
import com.artemis.managers.TagManager
import com.badlogic.gdx.math.Vector3
import fr.zlandorf.asteroids.game.AsteroidsGame
import fr.zlandorf.asteroids.game.Groups
import fr.zlandorf.asteroids.game.Tags
import fr.zlandorf.asteroids.game.components.BoundsComponent
import fr.zlandorf.asteroids.game.components.CameraTargetComponent
import fr.zlandorf.asteroids.game.components.ControlComponent
import fr.zlandorf.asteroids.game.components.GunComponent
import fr.zlandorf.asteroids.game.components.MotionComponent
import fr.zlandorf.asteroids.game.components.ParentComponent
import fr.zlandorf.asteroids.game.components.TextureComponent
import fr.zlandorf.asteroids.game.components.TransformComponent
import fr.zlandorf.asteroids.game.domain.Transform
import fr.zlandorf.asteroids.game.services.polygon

fun World.createSpaceship(): Entity? {
    val spaceShip = edit(create())
            .add(TextureComponent(AsteroidsGame.assets.spaceShip))
            .add(TransformComponent(Transform(
                    position= Vector3(0f, 0f, 0f)
            )))
            .add(ControlComponent(
                    thrusterAcceleration = 100f,
                    backThrusterAcceleration = -100f,
                    turnThrusterAcceleration = 5f
            ))
            .add(MotionComponent(
                    maxSpeed = 200f
            ))
            .add(GunComponent(
                    coolDown = 0.4f,
                    bulletSpeed = 500f
            ))
            .add(CameraTargetComponent())
            .add(BoundsComponent(AsteroidsGame.assets.spaceShip.polygon()))
            .entity
    getSystem(TagManager::class.java).register(Tags.SPACE_SHIP, spaceShip)

    val groupManager = getSystem(GroupManager::class.java)
    groupManager.add(
            edit(create())
                    .add(TextureComponent(AsteroidsGame.assets.blip, layer = 1, visible = false))
                    .add(ParentComponent(spaceShip.id))
                    .add(TransformComponent(Transform(position= Vector3(-66f, -34f, 0f), rotation = 90f)))
                    .entity,
            Groups.TURN_LEFT_BLIP
    )

    groupManager.add(
            edit(create())
                    .add(TextureComponent(AsteroidsGame.assets.blip, layer = 1, visible = false))
                    .add(ParentComponent(spaceShip.id))
                    .add(TransformComponent(Transform(position= Vector3(66f, -34f, 0f), rotation = -90f)))
                    .entity,
            Groups.TURN_RIGHT_BLIP
    )

    groupManager.add(
            edit(create())
                    .add(TextureComponent(AsteroidsGame.assets.blip, layer = 1, visible = false))
                    .add(ParentComponent(spaceShip.id))
                    .add(TransformComponent(Transform(position= Vector3(-45f, -44f, 0f), rotation = -180f)))
                    .entity,
            Groups.FORWARDS_BLIP
    )

    groupManager.add(
            edit(create())
                    .add(TextureComponent(AsteroidsGame.assets.blip, layer = 1, visible = false))
                    .add(ParentComponent(spaceShip.id))
                    .add(TransformComponent(Transform(position= Vector3(45f, -44f, 0f), rotation = -180f)))
                    .entity,
            Groups.FORWARDS_BLIP
    )

    groupManager.add(
            edit(create())
                    .add(TextureComponent(AsteroidsGame.assets.blip, layer = 1, visible = false))
                    .add(ParentComponent(spaceShip.id))
                    .add(TransformComponent(Transform(position= Vector3(-45f, 7f, 0f))))
                    .entity,
            Groups.BACKWARDS_BLIP
    )

    groupManager.add(
            edit(create())
                    .add(TextureComponent(AsteroidsGame.assets.blip, layer = 1, visible = false))
                    .add(ParentComponent(spaceShip.id))
                    .add(TransformComponent(Transform(position= Vector3(45f, 7f, 0f))))
                    .entity,
            Groups.BACKWARDS_BLIP
    )

    return spaceShip
}
