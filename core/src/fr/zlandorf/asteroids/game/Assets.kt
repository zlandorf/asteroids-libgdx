package fr.zlandorf.asteroids.game

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture

class Assets(
        private val manager: AssetManager = AssetManager()
) {

    companion object {
        val jupiter = AssetDescriptor("jupiter.png", Texture::class.java)
        val alienPlanet = AssetDescriptor("alien-planet.png", Texture::class.java)
        val firePlanet = AssetDescriptor("fire-planet.png", Texture::class.java)
        val atlas = AssetDescriptor("atlas.png", Texture::class.java)
    }

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