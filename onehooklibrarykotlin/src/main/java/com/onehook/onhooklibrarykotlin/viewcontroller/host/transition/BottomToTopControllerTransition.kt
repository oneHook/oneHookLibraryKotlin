package com.onehook.onhooklibrarykotlin.viewcontroller.host.transition

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.onehook.onhooklibrarykotlin.viewcontroller.host.ControllerTransition

class BottomToTopControllerTransition : ControllerTransition() {

    var dismissMoveRatio = 0.15f
    var dim = true
    var dimRatio = 1f

    override fun createEnteringAnimation(context: TransitionContext): AnimatorSet {
        val frameHeight = context.frame.height().toFloat()
        return AnimatorSet().also {
            it.duration = animationDuration
            val animations = arrayListOf<Animator>(
                ObjectAnimator.ofFloat(context.toController.view, "translationY", frameHeight, 0f),
                ObjectAnimator.ofFloat(context.cover, "alpha", 0f, if (dim) dimRatio else 0f)
            )
            context.fromController?.view?.apply {
                animations.add(
                    ObjectAnimator.ofFloat(
                        this,
                        "translationY",
                        0f,
                        -frameHeight * dismissMoveRatio
                    )
                )
            }
            it.playTogether(animations)
        }
    }

    override fun onEnteringAnimationFinished(context: TransitionContext) {
        val frameHeight = context.frame.height().toFloat()
        context.cover.alpha = if (dim) dimRatio else 0f
        context.fromController?.apply {
            view.translationY = -frameHeight * dismissMoveRatio
        }
        context.toController.apply {
            view.translationY = 0f
        }
    }

    override fun createExitingAnimation(context: TransitionContext): AnimatorSet {
        val frameHeight = context.frame.height().toFloat()
        return AnimatorSet().also {
            it.duration = animationDuration
            val animations = arrayListOf<Animator>(
                ObjectAnimator.ofFloat(context.toController.view, "translationY", 0f, frameHeight),
                ObjectAnimator.ofFloat(context.cover, "alpha", if (dim) dimRatio else 0f, 0f)
            )
            context.fromController?.view?.apply {
                animations.add(
                    ObjectAnimator.ofFloat(
                        this,
                        "translationY",
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
            view.translationY = 0f
        }
        context.toController.apply {
            view.translationY = context.frame.height().toFloat()
        }
    }
}