package fr.zlandorf.asteroids.game.components

import com.artemis.Component
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable

class TiledComponent(
        var tile: TiledDrawable? = null
) : Component()