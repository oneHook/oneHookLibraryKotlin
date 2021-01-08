package com.onehook.onehooklib.ui.controllers

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.onehook.onehooklib.ui.reusable.BaseViewController
import com.onehook.onehooklib.ui.reusable.LargeTextView
import com.onehook.onehooklib.ui.reusable.RoundedSolidButton
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.view.EXACTLY
import com.onehook.onhooklibrarykotlin.view.setFrame
import kotlin.random.Random

class ViewInflateByManualCodeDemoViewController : BaseViewController() {

    private val view1: LargeTextView by lazy {
        LargeTextView(context).apply {
            text = "1"
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setBackgroundColor(Color.RED)
        }
    }

    private val view2: LargeTextView by lazy {
        LargeTextView(context).apply {
            text = "2"
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setBackgroundColor(Color.BLUE)
        }
    }

    private val updateButton: RoundedSolidButton by lazy {
        RoundedSolidButton(context, Color.BLUE).apply {
            text = "Animate"
            setTextColor(Color.WHITE)
            setOnClickListener {
                animateViews()
            }
        }
    }

    override fun viewDidLoad(view: View) {
        super.viewDidLoad(view)
        (view as? ViewGroup)?.apply {
            setBackgroundColor(Color.WHITE)
            addView(view1)
            addView(view2)
            addView(updateButton)
        }
    }

    override fun viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()

        view1.measure(EXACTLY(dp(100)), EXACTLY(dp(25)))
        view1.setFrame(dp(50), dp(50), dp(100), dp(25))
        view2.measure(EXACTLY(dp(50)), EXACTLY(dp(125)))
        view2.setFrame(dp(50), dp(100), dp(50), dp(125))

        updateButton.measure(EXACTLY(dp(200)), EXACTLY(dp(50)))
        updateButton.setFrame(
            (view.width - updateButton.measuredWidth) / 2,
            (view.height - updateButton.measuredHeight - activity.safeArea.bottom),
            dp(200),
            dp(50)
        )
    }

    private fun animateViews() {
        val views = arrayListOf<View>(view1, view2)
        views.forEach { child ->
            val x = Random.nextInt(0, view.width / 2)
            val y = Random.nextInt(0, view.height / 2)
            val width = Random.nextInt(dp(50), dp(300))
            val height = Random.nextInt(dp(50), dp(300))

            val a = ValueAnimator.ofObject(
                RectEvaluator(),
                Rect(child.left, child.top, child.right, child.bottom),
                Rect(x, y, x + width, y + height)
            )
            a.duration = 200
            println("XXX start from ${child.width} ${child.height}")
            println("XXX final target ${width} ${height}")
            a.addUpdateListener {
                val rect = it.animatedValue as Rect
                println("XXX ${it.animatedFraction} ${rect.width()} ${rect.height()}")
                child.measure(EXACTLY(rect.width()), EXACTLY(rect.height()))
                child.setFrame(rect.left, rect.top, rect.width(), rect.height())
            }

            a.start()

//            val rect = Rect(x, y, x + width, y + height)
//            child.measure(EXACTLY(rect.width()), EXACTLY(rect.height()))
//            child.setFrame(rect.left, rect.top, rect.width(), rect.height())
            return
        }
    }
}

class RectEvaluator : TypeEvaluator<Rect> {
    override fun evaluate(fraction: Float, startFrame: Rect?, targetFrame: Rect?): Rect {
        if (startFrame != null && targetFrame != null) {
            val left = startFrame.left + (targetFrame.left - startFrame.left) * fraction
            val top = startFrame.top + (targetFrame.top - startFrame.top) * fraction
            val right = startFrame.right + (targetFrame.right - startFrame.right) * fraction
            val bottom = startFrame.bottom + (targetFrame.bottom - startFrame.bottom) * fraction
            return Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        }
        return Rect()
    }

}

//class FrameAnimator(
//    val view: View,
//    val targetFrame: Rect
//) : TimeAnimator() {
//
//    private val startFrame = Rect(view.left, view.top, view.right, view.bottom)
//
//    init {
//        duration = 500
//        interpolator = LinearInterpolator()
//        println("XXX animation created")
//    }
//
//
//
//    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
//        println("XXX ${interpolatedTime}")
//        val left = startFrame.left + (targetFrame.left - startFrame.left) * interpolatedTime
//        val top = startFrame.top + (targetFrame.top - startFrame.top) * interpolatedTime
//        val right = startFrame.right + (targetFrame.right - startFrame.right) * interpolatedTime
//        val bottom = startFrame.bottom + (targetFrame.bottom - startFrame.bottom) * interpolatedTime
//        view.measure(EXACTLY(right - left), EXACTLY(bottom - top))
//        view.layout(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
//    }
//}
