package com.onehook.onehooklib.ui.reusable

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatButton
import com.onehook.onhooklibrarykotlin.graphics.DrawableFactory
import com.onehook.onhooklibrarykotlin.utils.dpf

open class RoundedSolidButton(context: Context, @ColorInt color: Int) : AppCompatButton(context) {
    init {
        background = DrawableFactory.roundedRippleDrawable(color, Color.GRAY, dpf(25))
    }
}