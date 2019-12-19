package com.onehook.onhooklibrarykotlin.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatTextView

/* Layout Related */

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

/* Keyboard Related */

fun View.dismissKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    if (imm != null) {
        requestFocus()
        imm.showSoftInput(this, 0)
    }
}

fun View.toggleSoftInput(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.toggleSoftInput(0, 0)
}
