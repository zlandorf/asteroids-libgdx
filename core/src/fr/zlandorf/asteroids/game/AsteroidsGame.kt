package fr.zlandorf.asteroids.game

import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable
import com.badlogic.gdx.utils.viewport.ExtendViewport
import fr.zlandorf.asteroids.game.components.*
import fr.zlandorf.asteroids.game.systems.*

class AsteroidsGame : ApplicationAdapter() {
    private var batch: SpriteBatch? = null
    private var world: World? = null

    private val camera = OrthographicCamera()
    private val viewport = ExtendViewport(500f, 500f, camera)

    private val assets = Assets()

    override fun create() {
        assets.load()
        assets.finishLoading()

        batch = SpriteBatch()
        world = World(WorldConfigurationBuilder()
                .with(CameraSystem(camera, batch!!))
                .with(BackgroundRenderingSystem(camera, batch!!, TiledDrawable(assets.spaceTile)))
                .with(RenderingSystem(camera, batch!!))
                .with(MotionSystem())
                .with(SpaceshipControlSystem())
                .build())

        createSpaceship()
        createPlanets()
    }

    override fun render() {
        Gdx.gl.run {
            glClearColor(0f, 0f, .5f, 1f)
            glClear(GL20.GL_COLOR_BUFFER_BIT)
        }

        batch?.begin()
        world?.run {
            setDelta(Gdx.graphics.deltaTime)
            process()
        }
        batch?.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        batch?.projectionMatrix = camera.combined
    }

    override fun dispose() {
        batch?.dispose()
        world?.dispose()
        assets.dispose()
    }

    private fun createPlanets() {
        world?.run {
            edit(create())
                    .add(TextureComponent(TextureRegion(assets.get(Assets.firePlanet))))
                    .add(TransformComponent(
                            position = Vector3(Gdx.graphics.width / 2f, Gdx.graphics.height / 2f, 90f),
                            scale = Vector2(5f, 5f)
                    ))
        }
    }

    private fun createSpaceship() {
        world?.run {
            edit(create())
                    .add(TextureComponent(assets.spaceShip))
                    .add(TransformComponent(position= Vector3(Gdx.graphics.width / 2f, Gdx.graphics.height / 2f, 1f)))
                    .add(SpaceshipControlComponent(
                            thrusterAcceleration = 100f,
                            backThrusterAcceleration = -100f,
                            turnThrusterAcceleration = 5f
                    ))
                    .add(MotionComponent())
                    .add(CameraTargetComponent())

            edit(create())
                    .add(TextureComponent(TextureRegion(assets.get(Assets.atlas), 266, 153, 122, 77)))
                    .add(TransformComponent(position= Vector3(Gdx.graphics.width / 2f, Gdx.graphics.height / 2f, 89.9f)))
        }
    }

}
