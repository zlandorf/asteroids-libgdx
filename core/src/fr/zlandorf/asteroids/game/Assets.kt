package fr.zlandorf.asteroids.game

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Assets {

    companion object {
        private val manager: AssetManager = AssetManager()
        val jupiter = AssetDescriptor("jupiter.png", Texture::class.java)
        val alienPlanet = AssetDescriptor("alien-planet.png", Texture::class.java)
        val firePlanet = AssetDescriptor("fire-planet.png", Texture::class.java)
        val atlas = AssetDescriptor("atlas.txt", TextureAtlas::class.java)
    }

    val spaceShip get() = manager.get(atlas).findRegion("spaceship") ?: throw AssetNotFoundException()
    val spaceTile get() = manager.get(atlas).findRegion("space") ?: throw AssetNotFoundException()
    val blip get() = manager.get(atlas).findRegion("blip") ?: throw AssetNotFoundException()
    val bullet get() = manager.get(atlas).findRegion("projectile") ?: throw AssetNotFoundException()
    val asteroid get() = Animation<TextureRegion>(
            0.1f,
            manager.get(atlas).findRegions("asteroid") ?: throw AssetNotFoundException(),
            Animation.PlayMode.LOOP
    )
    val explosion get() = Animation<TextureRegion>(
            0.15f,
            manager.get(atlas).findRegions("explosion") ?: throw AssetNotFoundException(),
            Animation.PlayMode.NORMAL
    )

    fun <T> get(descriptor: AssetDescriptor<T>) = manager.get(descriptor)!!

    fun load() {
        manager.load(jupiter)
        manager.load(alienPlanet)
        manager.load(firePlanet)
        manager.load(atlas)
    }

    fun finishLoading() = manager.finishLoading()

    fun dispose() {
        manager.dispose()
    }

}

class AssetNotFoundException : Throwable()
