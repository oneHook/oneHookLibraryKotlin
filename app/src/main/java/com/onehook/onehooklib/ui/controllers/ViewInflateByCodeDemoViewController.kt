package com.onehook.onehooklib.ui.controllers

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.onehook.onehooklib.ui.reusable.BaseViewController
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.utils.dpf
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
                    marginBottom = dp(150)
                }.frameLayoutLp()
            })

            addView(LinearLayout(context).apply {
                layoutParams = LP().apply {
                    width = MATCH_PARENT
                    layoutGravity = Gravity.BOTTOM
                    marginBottom = activity.safeArea.bottom
                }.frameLayoutLp()
                orientation = LinearLayout.VERTICAL
                setBackgroundColor(Color.RED)
                addView(AppCompatTextView(context).apply {
                    layoutParams = LP().apply {
                        width = MATCH_PARENT
                        margin = dp(10)
                    }.linearLayoutLp()
                    textSize = dpf(8)
                    text = "MATCH PARENT"
                    setBackgroundColor(Color.GREEN)
                })
                addView(AppCompatTextView(context).apply {
                    layoutParams = LP().apply {
                        layoutGravity = Gravity.CENTER_HORIZONTAL
                    }.linearLayoutLp()
                    textSize = dpf(8)
                    text = "WRAP|CENTER"
                    setBackgroundColor(Color.GRAY)
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