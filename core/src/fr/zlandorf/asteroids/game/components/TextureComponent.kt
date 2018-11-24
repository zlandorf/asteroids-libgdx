package fr.zlandorf.asteroids.game.components

import com.artemis.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion

data class TextureComponent(
        var texture: TextureRegion? = null,
        var layer: Int = 0,
        var visible: Boolean = true
) : Component()