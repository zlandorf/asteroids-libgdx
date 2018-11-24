package fr.zlandorf.asteroids.game

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Assets {

    companion object {
        private val manager: AssetManager = AssetManager()
        val jupiter = AssetDescriptor("jupiter.png", Texture::class.java)
        val alienPlanet = AssetDescriptor("alien-planet.png", Texture::class.java)
        val firePlanet = AssetDescriptor("fire-planet.png", Texture::class.java)
        val atlas = AssetDescriptor("atlas.png", Texture::class.java)
    }

    val spaceShip get() = TextureRegion(manager.get(atlas), 266, 153, 122, 77)
    val spaceTile get() = TextureRegion(manager.get(atlas), 2, 232, 255, 255)

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