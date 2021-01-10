package com.onehook.onhooklibrarykotlin.viewcontroller.host.transition

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.onehook.onhooklibrarykotlin.viewcontroller.host.ControllerTransition

class BottomToTopControllerTransition : ControllerTransition() {

    override fun createEnteringAnimation(context: TransitionContext): AnimatorSet {
//        context.toController.apply {
//            view.translationY = context.frame.height().toFloat()
//        }
        val height = context.frame.height().toFloat()
        return AnimatorSet().also {
            it.duration = animationDuration
            it.playTogether(
                ObjectAnimator.ofFloat(context.toController.view, "translationY", height, 0f),
                ObjectAnimator.ofFloat(context.cover, "alpha", 0f, 1f)
            )
        }
    }

    override fun onEnteringAnimationFinished(context: TransitionContext) {
        context.toController.apply {
            view.translationY = 0f
        }
    }

    override fun createExitingAnimation(context: TransitionContext): AnimatorSet {
        val height = context.frame.height().toFloat()
        return AnimatorSet().also {
            it.duration = animationDuration
            it.playTogether(
                ObjectAnimator.ofFloat(context.toController.view, "translationY", 0f, height),
                ObjectAnimator.ofFloat(context.cover, "alpha", 1f, 0f)
            )
        }
    }

    override fun onExitingAnimationFinished(context: TransitionContext) {
        context.toController.apply {
            view.translationY = context.frame.height().toFloat()
        }
    }
}