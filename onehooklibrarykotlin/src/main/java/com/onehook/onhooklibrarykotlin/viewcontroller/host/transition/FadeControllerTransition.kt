package com.onehook.onhooklibrarykotlin.viewcontroller.host.transition

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.onehook.onhooklibrarykotlin.viewcontroller.host.ControllerTransition

class FadeControllerTransition : ControllerTransition() {

    var shouldFadeoutFrom = true

    override fun createEnteringAnimation(context: TransitionContext): AnimatorSet {
        return AnimatorSet().also {
            it.duration = animationDuration
            val animations = arrayListOf<Animator>(
                ObjectAnimator.ofFloat(context.toController.view, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(context.cover, "alpha", 0f, 1f)
            )
            context.fromController?.view?.apply {
                animations.add(
                    ObjectAnimator.ofFloat(
                        this,
                        "alpha",
                        if (shouldFadeoutFrom) 0f else 1f
                    )
                )
            }
            it.playTogether(animations)
        }
    }

    override fun onEnteringAnimationFinished(context: TransitionContext) {
        context.cover.alpha = 1f
        context.fromController?.apply {
            view.alpha = if (shouldFadeoutFrom) 0f else 1f
        }
        context.toController.apply {
            view.alpha = 1f
        }
    }

    override fun createExitingAnimation(context: TransitionContext): AnimatorSet {
        return AnimatorSet().also {
            it.duration = animationDuration
            val animations = arrayListOf<Animator>(
                ObjectAnimator.ofFloat(context.toController.view, "alpha", 0f),
                ObjectAnimator.ofFloat(context.cover, "alpha", 0f)
            )
            context.fromController?.view?.apply {
                animations.add(
                    ObjectAnimator.ofFloat(
                        this,
                        "alpha",
                        1f
                    )
                )
            }
            it.playTogether(animations)
        }
    }

    override fun onExitingAnimationFinished(context: TransitionContext) {
        context.cover.alpha = 0f
        context.fromController?.apply {
            view.alpha = 1f
        }
        context.toController.apply {
            view.alpha = 0f
        }
    }
}