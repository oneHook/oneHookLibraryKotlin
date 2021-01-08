package com.onehook.onehooklib.ui.controllers

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.setPadding
import com.onehook.onehooklib.ui.reusable.BaseViewController
import com.onehook.onehooklib.ui.reusable.LargeTextView
import com.onehook.onhooklibrarykotlin.graphics.DrawableFactory
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.view.LP
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT

class ViewInflateByCodeDemoViewController : BaseViewController() {

    private val contentView: ViewGroup by lazy {
        FrameLayout(context).apply {
            setBackgroundColor(Color.WHITE)
            addView(AppCompatImageView(context).apply {
                layoutParams = LP().apply {
                    width = dp(150)
                    height = dp(150)
                    setBackgroundColor(Color.BLUE)
                    layoutGravity = Gravity.CENTER
                    bottomMargin = dp(150)
                }.frameLayoutLp()
            })

            addView(LinearLayout(context).apply {
                layoutParams = LP().apply {
                    width = MATCH_PARENT
                    layoutGravity = Gravity.BOTTOM
                    bottomMargin = activity.safeArea.bottom
                }.frameLayoutLp()
                orientation = LinearLayout.VERTICAL
                setBackgroundColor(Color.RED)
                addView(LargeTextView(context).apply {
                    layoutParams = LP().apply {
                        width = MATCH_PARENT
                        margin = dp(10)
                    }.linearLayoutLp()
                    text = "MATCH PARENT"
                    setBackgroundColor(Color.GREEN)
                })
                addView(LargeTextView(context).apply {
                    layoutParams = LP().apply {
                        layoutGravity = Gravity.CENTER_HORIZONTAL
                    }.linearLayoutLp()
                    text = "WRAP|CENTER"
                    setBackgroundColor(Color.GRAY)
                })

                addView(AppCompatButton(context).apply {
                    layoutParams = LP().apply {
                        topMargin = dp(20)
                        layoutGravity = Gravity.CENTER_HORIZONTAL
                    }.linearLayoutLp()
                    setPadding(dp(10))
                    text = "Click Me"
                    background =
                        DrawableFactory.roundedRippleDrawable(
                            Color.YELLOW,
                            Color.GRAY
                        )
                })
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return contentView
    }

    override fun viewDidLoad(view: View) {
        super.viewDidLoad(view)
    }
}