package fr.zlandorf.asteroids.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import fr.zlandorf.asteroids.game.AsteroidsGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            width= 1600
            height= 900
        }
        LwjglApplication(AsteroidsGame(), config)
    }
}
