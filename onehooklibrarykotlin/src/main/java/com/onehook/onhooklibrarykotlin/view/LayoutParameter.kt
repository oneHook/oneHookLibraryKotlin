package com.onehook.onhooklibrarykotlin.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.onehook.onhooklibrarykotlin.R

/* Layout Related */

const val MATCH_PARENT = -1
const val WRAP_CONTENT = -2

class LP : ViewGroup.MarginLayoutParams {

    var layoutGravity: Int
    var layoutWeight: Float

    var margin: Int = 0
        set(newValue) {
            field = newValue
            leftMargin = newValue
            rightMargin = newValue
            topMargin = newValue
            bottomMargin = newValue
        }

    init {
        layoutGravity = 0
        layoutWeight = 0f
    }

    constructor() : super(WRAP_CONTENT, WRAP_CONTENT)

    constructor(width: Int, height: Int) : super(width, height)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewInfo)
        layoutGravity = a.getInteger(R.styleable.ViewInfo_android_layout_gravity, 0)
        layoutWeight = a.getFloat(R.styleable.ViewInfo_android_layout_weight, 0.0f)
        a.recycle()
    }

    fun linearLayoutLp(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(this).also {
            it.gravity = layoutGravity
            it.weight = layoutWeight
        }
    }

    fun frameLayoutLp(): FrameLayout.LayoutParams {
        return FrameLayout.LayoutParams(this).also {
            it.gravity = layoutGravity
        }
    }
}

fun EXACTLY(dimension: Int): Int {
    return View.MeasureSpec.makeMeasureSpec(dimension, View.MeasureSpec.EXACTLY)
}

fun EXACTLY(dimension: Float): Int {
    return View.MeasureSpec.makeMeasureSpec(dimension.toInt(), View.MeasureSpec.EXACTLY)
}

fun AT_MOST(dimension: Int): Int {
    return View.MeasureSpec.makeMeasureSpec(dimension, View.MeasureSpec.AT_MOST)
}

fun AT_MOST(dimension: Float): Int {
    return View.MeasureSpec.makeMeasureSpec(dimension.toInt(), View.MeasureSpec.AT_MOST)
}