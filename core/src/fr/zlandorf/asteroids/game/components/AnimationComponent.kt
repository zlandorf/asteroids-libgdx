package fr.zlandorf.asteroids.game.components

import com.artemis.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion

data class AnimationComponent(
        var animation: Animation<TextureRegion>? = null,
        var time: Float = 0f
) : Component()