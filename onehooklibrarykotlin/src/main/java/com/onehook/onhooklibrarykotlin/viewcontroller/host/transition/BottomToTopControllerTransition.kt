package com.onehook.onhooklibrarykotlin.viewcontroller.host.transition

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import com.onehook.onhooklibrarykotlin.viewcontroller.host.ControllerTransition

class BottomToTopControllerTransition : ControllerTransition() {

    var dismissMoveRatio = 0.15f
    var dim = true
    var dimRatio = 0.5f

    override fun createEnteringAnimation(context: TransitionContext): AnimatorSet {
        val frameHeight = context.frame.height().toFloat()
        return AnimatorSet().also {
            it.duration = animationDuration
            val animations = arrayListOf<Animator>(
                ObjectAnimator.ofFloat(context.toController.view, "translationY", 0f),
                ObjectAnimator.ofFloat(context.cover, "alpha", if (dim) dimRatio else 0f)
            )
            context.fromController?.view?.apply {
                animations.add(
                    ObjectAnimator.ofFloat(
                        this,
                        "translationY",
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
                ObjectAnimator.ofFloat(context.toController.view, "translationY", frameHeight),
                ObjectAnimator.ofFloat(context.cover, "alpha", 0f)
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

    private fun verticalProgress(context: TransitionContext, start: Point, current: Point): Float =
        (current.y - start.y) / context.frame.height().toFloat()

    override fun updateInteractiveDismiss(
        context: TransitionContext,
        start: Point,
        current: Point
    ) {
        val progress = verticalProgress(context, start, current)
        context.toController.view.translationY = (current.y - start.y).toFloat()
        context.fromController?.view?.translationY = -context.frame.height() * dismissMoveRatio * (1 - progress)
        context.cover.alpha = (1 - progress) * dimRatio
    }

    override fun finishInteractiveDismiss(
        context: TransitionContext,
        start: Point,
        current: Point
    ): Boolean = verticalProgress(context, start, current) > 0.15
}