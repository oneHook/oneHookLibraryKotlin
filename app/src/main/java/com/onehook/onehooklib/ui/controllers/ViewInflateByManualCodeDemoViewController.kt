package com.onehook.onehooklib.ui.controllers

import android.animation.Animator
import android.animation.AnimatorSet
import android.graphics.Color
import android.graphics.Rect
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onehook.onehooklib.ui.reusable.BaseDetailViewController
import com.onehook.onehooklib.ui.reusable.LargeTextView
import com.onehook.onehooklib.ui.reusable.RoundedSolidButton
import com.onehook.onhooklibrarykotlin.animation.frameAnimation
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.view.EXACTLY
import com.onehook.onhooklibrarykotlin.view.LP
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import com.onehook.onhooklibrarykotlin.view.setFrame
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.EDView
import kotlin.random.Random

class ViewInflateByManualCodeDemoViewController : BaseDetailViewController() {

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

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View {
        return EDView(this).apply {
            layoutParams = LP().apply {
                width = MATCH_PARENT
                height = MATCH_PARENT
            }.frameLayoutLp()
        }
    }

    override fun viewDidLoad(view: View) {
        super.viewDidLoad(view)
        toolbar.title.getOrBuild().text = "Create View By Manual Layout"
        (_contentView as? ViewGroup)?.apply {
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
            updateButton.measuredWidth,
            updateButton.measuredHeight
        )
    }

    private fun animateViews() {
        val views = arrayListOf<View>(view1, view2)
        val animations = arrayListOf<Animator>()
        views.forEach { child ->
            val x = Random.nextInt(0, view.width / 2)
            val y = Random.nextInt(0, view.height / 2)
//            val width = child.measuredWidth
//            val height = child.measuredHeight
            val width = Random.nextInt(dp(50), dp(300))
            val height = Random.nextInt(dp(50), dp(300))
            animations.add(child.frameAnimation(Rect(x, y, x + width, y + height)))
        }
        val aset = AnimatorSet()
        aset.playSequentially(animations)
        aset.start()
    }
}