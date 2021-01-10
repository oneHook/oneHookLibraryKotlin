package com.onehook.onhooklibrarykotlin.viewcontroller.host.transition

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.onehook.onhooklibrarykotlin.viewcontroller.host.ControllerTransition

class LeftToRightControllerTransition : ControllerTransition() {

    var dismissMoveRatio = 0.15f
    var dim = true
    var dimRatio = 0.5f

    override fun createEnteringAnimation(context: TransitionContext): AnimatorSet {
        val frameWidth = context.frame.width().toFloat()
        return AnimatorSet().also {
            it.duration = animationDuration
            val animations = arrayListOf<Animator>(
                ObjectAnimator.ofFloat(context.toController.view, "translationX", frameWidth, 0f),
                ObjectAnimator.ofFloat(context.cover, "alpha", 0f, if (dim) dimRatio else 0f)
            )
            context.fromController?.view?.apply {
                animations.add(
                    ObjectAnimator.ofFloat(
                        this,
                        "translationX",
                        0f,
                        -frameWidth * dismissMoveRatio
                    )
                )
            }
            it.playTogether(animations)
        }
    }

    override fun onEnteringAnimationFinished(context: TransitionContext) {
        val frameWidth = context.frame.width().toFloat()
        context.cover.alpha = if (dim) dimRatio else 0f
        context.fromController?.apply {
            view.translationX = -frameWidth * dismissMoveRatio
        }
        context.toController.apply {
            view.translationX = 0f
        }
    }

    override fun createExitingAnimation(context: TransitionContext): AnimatorSet {
        val frameWidth = context.frame.width().toFloat()
        return AnimatorSet().also {
            it.duration = animationDuration
            val animations = arrayListOf<Animator>(
                ObjectAnimator.ofFloat(context.toController.view, "translationX", 0f, frameWidth),
                ObjectAnimator.ofFloat(context.cover, "alpha", if (dim) dimRatio else 0f, 0f)
            )
            context.fromController?.view?.apply {
                animations.add(
                    ObjectAnimator.ofFloat(
                        this,
                        "translationX",
                        0f
                    )
                )
            }
            it.playTogether(animations)
        }
    }

    override fun onExitingAnimationFinished(context: TransitionContext) {
        context.cover.alpha = 0f
        context.fromController?.apply {
            view.translationX = 0f
        }
        context.toController.apply {
            view.translationX = context.frame.width().toFloat()
        }
    }
}