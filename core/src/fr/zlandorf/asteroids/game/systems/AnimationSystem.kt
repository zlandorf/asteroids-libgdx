package fr.zlandorf.asteroids.game.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import fr.zlandorf.asteroids.game.components.AnimationComponent
import fr.zlandorf.asteroids.game.components.TextureComponent
import fr.zlandorf.asteroids.game.domain.AnimationEndedEvent
import net.mostlyoriginal.api.event.common.EventSystem

class AnimationSystem : IteratingSystem(
        Aspect.all(TextureComponent::class.java, AnimationComponent::class.java)
) {

    private lateinit var textureMapper: ComponentMapper<TextureComponent>
    private lateinit var animationMapper: ComponentMapper<AnimationComponent>

    private lateinit var eventSystem: EventSystem

    private val toDeleteAfterAnimation = mutableSetOf<Int>()

    override fun process(entityId: Int) {
        val animationComponent = animationMapper.get(entityId)
        animationComponent.time += world.delta

        animationComponent.animation?.run {
            if (isEnabled) {
                textureMapper.get(entityId).texture = getKeyFrame(animationComponent.time)
                if (isAnimationFinished(animationComponent.time) && !isLooping()) {
                    eventSystem.dispatch(AnimationEndedEvent(entityId, this))
                    if (toDeleteAfterAnimation.contains(entityId)) {
                        world.delete(entityId)
                        toDeleteAfterAnimation.remove(entityId)
                    }
                }
            }
        }
    }

    fun deleteAfterAnimation(entityId: Int) = toDeleteAfterAnimation.add(entityId)

    private fun Animation<TextureRegion>.isLooping() = playMode != Animation.PlayMode.NORMAL && playMode != Animation.PlayMode.REVERSED
}