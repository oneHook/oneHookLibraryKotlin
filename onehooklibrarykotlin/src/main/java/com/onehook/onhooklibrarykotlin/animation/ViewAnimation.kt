package com.onehook.onhooklibrarykotlin.animation

import android.animation.Animator
import android.animation.RectEvaluator
import android.animation.ValueAnimator
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import com.onehook.onhooklibrarykotlin.view.EXACTLY
import com.onehook.onhooklibrarykotlin.view.setFrame

fun View.frameAnimation(target: Rect): Animator {
    return ValueAnimator.ofObject(
        RectEvaluator(),
        Rect(left, top, right, bottom),
        target
    ).apply {
        addUpdateListener {
            val rect = it.animatedValue as Rect
            measure(EXACTLY(rect.width()), EXACTLY(rect.height()))
            setFrame(frame = rect)
        }
    }
}

fun View.positionAnimation(position: Point): Animator {
    return ValueAnimator.ofObject(
        RectEvaluator(),
        Rect(left, top, right, bottom),
        Rect(position.x, position.y, position.x + measuredWidth, position.y + measuredHeight)
    ).apply {
        addUpdateListener {
            val rect = it.animatedValue as Rect
            setFrame(rect)
        }
    }
}