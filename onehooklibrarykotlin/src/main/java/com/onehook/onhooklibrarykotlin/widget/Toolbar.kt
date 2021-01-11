package com.onehook.onhooklibrarykotlin.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.onehook.onhooklibrarykotlin.utils.optionalBuilder
import com.onehook.onhooklibrarykotlin.view.LP

open class Toolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyle, defStyleRes) {

    val title = optionalBuilder {
        TextView(context).also {
            it.layoutParams = LP().apply {
                layoutGravity = Gravity.CENTER
            }.frameLayoutLp()
            it.textSize = 14f
            it.setTextColor(Color.BLACK)
            addView(it)
        }
    }

    init {
        setBackgroundColor(Color.WHITE)
    }
}
