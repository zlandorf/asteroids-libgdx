package fr.zlandorf.asteroids.game.entities

import com.artemis.EntityEdit
import com.artemis.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector3
import fr.zlandorf.asteroids.game.Assets
import fr.zlandorf.asteroids.game.AsteroidsGame
import fr.zlandorf.asteroids.game.components.TextureComponent
import fr.zlandorf.asteroids.game.components.TransformComponent
import fr.zlandorf.asteroids.game.domain.Transform

fun World.createPlanets() {
    createFirePlanet()
    createAlienPlanet()
}

fun World.createFirePlanet(): EntityEdit? = edit(create())
        .add(TextureComponent(TextureRegion(AsteroidsGame.assets.get(Assets.firePlanet))))
        .add(TransformComponent(Transform(
                position = Vector3(5000f, Gdx.graphics.height / 2f, 90f),
                scale = Vector3(5f, 5f, 1f)
        )))


fun World.createAlienPlanet(): EntityEdit? = edit(create())
        .add(TextureComponent(TextureRegion(AsteroidsGame.assets.get(Assets.alienPlanet))))
        .add(TransformComponent(Transform(
                position = Vector3(-10000f, 4000f, 95f),
                scale = Vector3(2f, 2f, 1f)
        )))
