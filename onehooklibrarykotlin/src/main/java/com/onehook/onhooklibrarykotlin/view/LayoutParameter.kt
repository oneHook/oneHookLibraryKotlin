package com.onehook.onhooklibrarykotlin.view

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout

/* Layout Related */

const val MATCH_PARENT = -1
const val WRAP_CONTENT = -2

class LP : ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) {

    var marginStart: Int
    var marginEnd: Int
    var marginTop: Int
    var marginBottom: Int
    var layoutGravity: Int

    var margin: Int = 0
        set(newValue) {
            field = newValue
            marginStart = newValue
            marginEnd = newValue
            marginTop = newValue
            marginBottom = newValue
        }

    init {
        width = WRAP_CONTENT
        height = WRAP_CONTENT
        marginStart = 0
        marginEnd = 0
        marginTop = 0
        marginBottom = 0
        layoutGravity = 0
    }

    fun linearLayoutLp(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(width, height).also {
            it.setMargins(marginStart, marginTop, marginEnd, marginBottom)
            it.gravity = layoutGravity
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