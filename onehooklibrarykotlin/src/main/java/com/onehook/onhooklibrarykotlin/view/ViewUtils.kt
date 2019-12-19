package com.onehook.onhooklibrarykotlin.view

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

const val MATCH_PARENT = -1
const val WRAP_CONTENT = -2

fun View.setFrame(x: Int, y: Int, width: Int, height: Int) {
    layout(x, y, x + width, y + height)
}

fun EXACTLY(dimension: Int): Int {
    return View.MeasureSpec.makeMeasureSpec(dimension, View.MeasureSpec.EXACTLY)
}

fun AT_MOST(dimension: Int): Int {
    return View.MeasureSpec.makeMeasureSpec(dimension, View.MeasureSpec.AT_MOST)
}

fun AppCompatTextView.setDrawable(left: Drawable? = null,
                                  top: Drawable? = null,
                                  right: Drawable? = null,
                                  bottom: Drawable? = null) {
    setCompoundDrawables(left, top, right, bottom)
}