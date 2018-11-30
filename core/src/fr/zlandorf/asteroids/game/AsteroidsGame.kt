package fr.zlandorf.asteroids.game

import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.GroupManager
import com.artemis.managers.TagManager
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable
import com.badlogic.gdx.utils.viewport.ExtendViewport
import fr.zlandorf.asteroids.game.entities.createAsteroid
import fr.zlandorf.asteroids.game.entities.createFirePlanet
import fr.zlandorf.asteroids.game.entities.createPlanets
import fr.zlandorf.asteroids.game.entities.createSpaceship
import fr.zlandorf.asteroids.game.handlers.CollisionHandler
import fr.zlandorf.asteroids.game.services.TransformService
import fr.zlandorf.asteroids.game.systems.AnimationSystem
import fr.zlandorf.asteroids.game.systems.BackgroundRenderingSystem
import fr.zlandorf.asteroids.game.systems.BoundsRenderingSystem
import fr.zlandorf.asteroids.game.systems.CameraSystem
import fr.zlandorf.asteroids.game.systems.CollisionSystem
import fr.zlandorf.asteroids.game.systems.ControlSystem
import fr.zlandorf.asteroids.game.systems.GunSystem
import fr.zlandorf.asteroids.game.systems.MotionSystem
import fr.zlandorf.asteroids.game.systems.RenderingSystem
import net.mostlyoriginal.api.event.common.EventSystem

class AsteroidsGame : ApplicationAdapter() {
    companion object {
        val assets = Assets()
    }

    private var batch: SpriteBatch? = null
    private var world: World? = null

    private val camera = OrthographicCamera()
    private val viewport = ExtendViewport(700f, 700f, camera)

    private var debug = false

    override fun create() {
        assets.load()
        assets.finishLoading()

        batch = SpriteBatch()
        world = World(WorldConfigurationBuilder()
                .with(TransformService())
                .with(EventSystem())
                .with(TagManager()).with(GroupManager())
                .with(CameraSystem(camera, batch!!))
                .with(BackgroundRenderingSystem(camera, batch!!, TiledDrawable(assets.spaceTile)))
                .with(RenderingSystem(camera, batch!!))
                .apply {
                    if (debug) {
                        with(BoundsRenderingSystem(camera, batch!!))
                    }
                }
                .with(MotionSystem())
                .with(ControlSystem())
                .with(GunSystem())
                .with(AnimationSystem())
                .with(CollisionSystem())
                .with(CollisionHandler())
                .build())

        world?.createSpaceship()
        world?.createPlanets()
        world?.createAsteroid()
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

}
