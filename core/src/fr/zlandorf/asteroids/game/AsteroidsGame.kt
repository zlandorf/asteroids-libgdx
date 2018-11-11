package fr.zlandorf.asteroids.game

import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable
import com.badlogic.gdx.utils.viewport.ExtendViewport
import fr.zlandorf.asteroids.game.components.*
import fr.zlandorf.asteroids.game.systems.SpaceshipControlSystem
import fr.zlandorf.asteroids.game.systems.MotionSystem
import fr.zlandorf.asteroids.game.systems.CameraSystem
import fr.zlandorf.asteroids.game.systems.RenderingSystem

class AsteroidsGame : ApplicationAdapter() {
    private var batch: SpriteBatch? = null
    private var atlas: Texture? = null
    private var world: World? = null

    private val camera = OrthographicCamera()
    private val viewport = ExtendViewport(500f, 500f, camera)

    override fun create() {
        batch = SpriteBatch()
        atlas = Texture("atlas.png")
        world = World(WorldConfigurationBuilder()
                .with(CameraSystem(camera, batch!!))
                .with(RenderingSystem(camera, batch!!))
                .with(MotionSystem())
                .with(SpaceshipControlSystem())
                .build())

        createSpaceship()
        createBackground()
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
        atlas?.dispose()
        world?.dispose()
    }

    private fun createSpaceship() {
        world?.run {
            edit(create())
                    .add(TextureComponent(TextureRegion(atlas, 266, 153, 122, 77)))
                    .add(TransformComponent(position= Vector3(Gdx.graphics.width / 2f, Gdx.graphics.height / 2f, 1f)))
                    .add(SpaceshipControlComponent(
                            thrusterAcceleration = 100f,
                            backThrusterAcceleration = -100f,
                            turnThrusterAcceleration = 5f
                    ))
                    .add(MotionComponent())
                    .add(CameraTargetComponent())

            edit(create())
                    .add(TextureComponent(TextureRegion(atlas, 266, 153, 122, 77)))
                    .add(TransformComponent(position= Vector3(Gdx.graphics.width / 2f - 20f, Gdx.graphics.height / 2f - 20f, 1f)))
                    .add(MotionComponent())
        }
    }

    private fun createBackground() {
        world?.run {
            val texture = TextureRegion(atlas, 2, 232, 255, 255)
            val tiled = TiledDrawable(texture)

            edit(create())
                    .add(TiledComponent(tiled))
                    .add(TransformComponent(
                            position = Vector3(Gdx.graphics.width / 2f, Gdx.graphics.height / 2f, 10f),
                            scale = Vector2(1f, 1f)
                    ))
        }
    }
}
