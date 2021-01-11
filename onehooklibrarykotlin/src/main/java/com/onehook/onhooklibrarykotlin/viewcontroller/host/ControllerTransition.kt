package com.onehook.onhooklibrarykotlin.viewcontroller.host

import android.animation.AnimatorSet
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.ViewController

abstract class ControllerTransition {

    data class TransitionContext(
        val fromController: ViewController?,
        val toController: ViewController,
        val cover: View,
        val frame: Rect
    )

    var animationDuration: Long = 250
    abstract fun createEnteringAnimation(context: TransitionContext): AnimatorSet
    abstract fun onEnteringAnimationFinished(context: TransitionContext)

    abstract fun createExitingAnimation(context: TransitionContext): AnimatorSet
    abstract fun onExitingAnimationFinished(context: TransitionContext)

    open fun updateInteractiveDismiss(context: TransitionContext, start: Point, current: Point) {}
    open fun finishInteractiveDismiss(context: TransitionContext, start: Point, current: Point): Boolean = false
}