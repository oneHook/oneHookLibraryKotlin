package com.onehook.onhooklibrarykotlin.viewcontroller.host.transition

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
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
                ObjectAnimator.ofFloat(context.toController.view, "translationX", 0f),
                ObjectAnimator.ofFloat(context.cover, "alpha", if (dim) dimRatio else 0f)
            )
            context.fromController?.view?.apply {
                animations.add(
                    ObjectAnimator.ofFloat(
                        this,
                        "translationX",
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
                ObjectAnimator.ofFloat(context.toController.view, "translationX", frameWidth),
                ObjectAnimator.ofFloat(context.cover, "alpha", 0f)
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

    private fun horizontalProgress(context: TransitionContext, start: Point, current: Point): Float =
        (current.x - start.x) / context.frame.width().toFloat()

    override fun updateInteractiveDismiss(
        context: TransitionContext,
        start: Point,
        current: Point
    ) {
        val progress = horizontalProgress(context, start, current)
        context.toController.view.translationX = (current.x - start.x).toFloat()
        context.fromController?.view?.translationX = -context.frame.width() * dismissMoveRatio * (1 - progress)
        if (dim) {
            context.cover.alpha = (1 - progress) * dimRatio
        }
    }

    override fun finishInteractiveDismiss(
        context: TransitionContext,
        start: Point,
        current: Point
    ): Boolean = horizontalProgress(context, start, current) > 0.15
}