package com.onehook.onhooklibrarykotlin.viewcontroller.host

import android.animation.AnimatorSet
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

    var animationDuration: Long = 2000
    abstract fun createEnteringAnimation(context: TransitionContext): AnimatorSet
    abstract fun onEnteringAnimationFinished(context: TransitionContext)

    abstract fun createExitingAnimation(context: TransitionContext): AnimatorSet
    abstract fun onExitingAnimationFinished(context: TransitionContext)
}

//private fun createExitAnimation(
//    viewController: ViewController,
//    style: PresentationStyle
//): ObjectAnimator {
//    val width = measuredWidth.toFloat()
//    val height = measuredHeight.toFloat()
//
//    when (style.animation) {
//        PresentingAnimation.RIGHT -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "translationX", 0f, -disappearRatio * width
//            )
//        }
//        PresentingAnimation.RIGHT_REVEAL, PresentingAnimation.RIGHT_TRANSLATION -> {
//            bringChildToFront(viewController.view)
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "translationX", 0f, -width
//            )
//        }
//        PresentingAnimation.BOTTOM -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "translationY", 0f, -disappearRatio * height
//            )
//        }
//        PresentingAnimation.BOTTOM_FADE -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "alpha", 1f, 0f
//            )
//        }
//        else -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "alpha", 1f, 0f
//            )
//        }
//    }
//}
//
//private fun createEnterAnimation(
//    viewController: ViewController,
//    style: PresentationStyle
//): ObjectAnimator {
//    val width = measuredWidth.toFloat()
//    val height = measuredHeight.toFloat()
//
//    when (style.animation) {
//        PresentingAnimation.RIGHT -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "translationX", -disappearRatio * width, 0f
//            )
//        }
//        PresentingAnimation.RIGHT_REVEAL, PresentingAnimation.RIGHT_TRANSLATION -> {
//            bringChildToFront(viewController.view)
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "translationX", -width, 0f
//            )
//        }
//        PresentingAnimation.BOTTOM -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "translationY", -disappearRatio * height, 0f
//            )
//        }
//        PresentingAnimation.BOTTOM_FADE -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "alpha", 0f, 1f
//            )
//        }
//        else -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "alpha", 0f, 1f
//            )
//        }
//    }
//}
//
//private fun createPushAnimation(viewController: ViewController): ObjectAnimator {
//    val width = measuredWidth.toFloat()
//    val height = measuredHeight.toFloat()
//    val style = viewController.presentationStyle
//
//    when (style.animation) {
//        PresentingAnimation.RIGHT, PresentingAnimation.RIGHT_TRANSLATION -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "translationX", width, 0f
//            )
//        }
//        PresentingAnimation.RIGHT_REVEAL -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "translationX", 0f, 0f
//            )
//        }
//        PresentingAnimation.BOTTOM, PresentingAnimation.BOTTOM_FADE -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "translationY", height, 0f
//            )
//        }
//        else -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "alpha", 0f, 1f
//            )
//        }
//    }
//}
//
//private fun createPopAnimation(viewController: ViewController): ObjectAnimator {
//    val width = measuredWidth.toFloat()
//    val height = measuredHeight.toFloat()
//    val style = viewController.presentationStyle
//
//    when (style.animation) {
//        PresentingAnimation.RIGHT, PresentingAnimation.RIGHT_TRANSLATION -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "translationX", 0f, width
//            )
//        }
//        PresentingAnimation.RIGHT_REVEAL -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "translationX", 0f, 0f
//            )
//        }
//        PresentingAnimation.BOTTOM, PresentingAnimation.BOTTOM_FADE -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "translationY", 0f, height
//            )
//        }
//        else -> {
//            return ObjectAnimator.ofFloat(
//                viewController.view,
//                "alpha", 1f, 0f
//            )
//        }
//    }
//}