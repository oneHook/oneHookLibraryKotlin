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

class LP : ViewGroup.LayoutParams {

    var marginStart: Int
    var marginEnd: Int
    var marginTop: Int
    var marginBottom: Int
    var layoutGravity: Int
    var layoutWeight: Float

    var margin: Int = 0
        set(newValue) {
            field = newValue
            marginStart = newValue
            marginEnd = newValue
            marginTop = newValue
            marginBottom = newValue
        }

    init {
        marginStart = 0
        marginEnd = 0
        marginTop = 0
        marginBottom = 0
        layoutGravity = 0
        layoutWeight = 0f
    }

    constructor() : super(WRAP_CONTENT, WRAP_CONTENT)

    constructor(width: Int, height: Int) : super(width, height)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewInfo)
        val margin =
            a.getDimension(R.styleable.ViewInfo_android_layout_margin, 0f)
        margin.toInt().also {
            marginStart = it
            marginEnd = it
            marginTop = it
            marginBottom = it
        }
        marginStart =
            a.getDimension(R.styleable.ViewInfo_android_layout_marginStart, margin).toInt()
        marginEnd = a.getDimension(R.styleable.ViewInfo_android_layout_marginEnd, margin).toInt()
        marginTop = a.getDimension(R.styleable.ViewInfo_android_layout_marginTop, margin).toInt()
        marginBottom =
            a.getDimension(R.styleable.ViewInfo_android_layout_marginBottom, margin).toInt()
        layoutGravity = a.getInteger(R.styleable.ViewInfo_android_layout_gravity, 0)
        layoutWeight = a.getFloat(R.styleable.ViewInfo_android_layout_weight, 0.0f)
        a.recycle()
    }

    fun linearLayoutLp(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(width, height).also {
            it.setMargins(marginStart, marginTop, marginEnd, marginBottom)
            it.gravity = layoutGravity
            it.weight = layoutWeight
        }
    }

    fun frameLayoutLp(): FrameLayout.LayoutParams {
        return FrameLayout.LayoutParams(width, height).also {
            it.setMargins(marginStart, marginTop, marginEnd, marginBottom)
            it.gravity = layoutGravity
        }
    }
}

fun EXACTLY(dimension: Int): Int {
    return View.MeasureSpec.makeMeasureSpec(dimension, View.MeasureSpec.EXACTLY)
}

fun AT_MOST(dimension: Int): Int {
    return View.MeasureSpec.makeMeasureSpec(dimension, View.MeasureSpec.AT_MOST)
}