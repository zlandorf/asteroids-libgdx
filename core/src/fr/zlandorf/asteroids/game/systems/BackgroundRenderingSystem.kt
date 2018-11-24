package fr.zlandorf.asteroids.game.systems

import com.artemis.BaseSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable

class BackgroundRenderingSystem(
        private val camera: Camera,
        private val batch: SpriteBatch,
        private val backgroundTile: TiledDrawable,
        private val initialPosition: Vector3 = Vector3(0f, 0f, 95f)
) : BaseSystem() {

    override fun processSystem() {
        val position = initialPosition.cpy()
        val depthFactor = (100f - position.z) / 100f
        position.set(
                camera.position.x - (camera.position.x - position.x) * depthFactor,
                camera.position.y - (camera.position.y - position.y) * depthFactor,
                0f
        )

        val x = camera.position.x - (camera.viewportWidth / 2f) - ((camera.position.x - position.x) % backgroundTile.region.regionWidth)
        val y = camera.position.y - (camera.viewportHeight / 2f) - ((camera.position.y - position.y) % backgroundTile.region.regionHeight)
        backgroundTile.draw(
                batch,
                x - backgroundTile.region.regionWidth,
                y - backgroundTile.region.regionHeight,
                camera.viewportWidth + 2f * backgroundTile.minWidth,
                camera.viewportHeight + 2f * backgroundTile.minHeight
        )
    }

}