package fr.zlandorf.asteroids.game.domain

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import net.mostlyoriginal.api.event.common.Event

data class AnimationEndedEvent(
        val entityId: Int,
        val animation: Animation<TextureRegion>
): Event