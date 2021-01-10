package com.onehook.onhooklibrarykotlin.viewcontroller.host

import android.animation.Animator
import android.animation.AnimatorSet
import android.graphics.Rect
import com.onehook.onhooklibrarykotlin.utils.AnimationEndListener
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.ViewController

abstract class ControllerTransition {

    data class TransitionContext(
        val fromController: ViewController,
        val toController: ViewController,
        val frame: Rect
    )

    var animationDuration: Long = 250
    abstract fun createEnteringAnimation(context: TransitionContext): AnimatorSet
    abstract fun createExitingAnimation(context: TransitionContext): AnimatorSet

    fun startEnteringAnimation(fromController: ViewController, toController: ViewController, frame: Rect, completion: () -> Unit) {
        createEnteringAnimation(
            TransitionContext(fromController = fromController, toController = toController, frame = frame)
        ).apply {
            addListener(object: AnimationEndListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    completion()
                }
            })
            start()
        }
    }

    fun startExitingAnimation(fromController: ViewController, toController: ViewController, frame: Rect, completion: () -> Unit) {
        createExitingAnimation(
            TransitionContext(fromController = fromController, toController = toController, frame = frame)
        ).apply {
            addListener(object: AnimationEndListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    completion()
                }
            })
            start()
        }
    }
}