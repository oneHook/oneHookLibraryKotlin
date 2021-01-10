package com.onehook.onhooklibrarykotlin.viewcontroller.host.transition

import android.animation.AnimatorSet
import com.onehook.onhooklibrarykotlin.viewcontroller.host.ControllerTransition

class BottomToTopControllerTransition : ControllerTransition() {

    override fun createEnteringAnimation(context: TransitionContext): AnimatorSet {
        return AnimatorSet()
    }

    override fun createExitingAnimation(context: TransitionContext): AnimatorSet {
        return AnimatorSet()
    }
}