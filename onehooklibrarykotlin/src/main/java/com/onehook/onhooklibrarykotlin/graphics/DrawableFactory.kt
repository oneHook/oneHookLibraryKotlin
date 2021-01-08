package com.onehook.onhooklibrarykotlin.graphics

import android.R
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import com.onehook.onhooklibrarykotlin.utils.dpf

class DrawableFactory {

    companion object {

        fun backgroundRippleDrawable(normalColor: Int, pressedColor: Int): RippleDrawable {
            return RippleDrawable(
                getPressedColorSelector(normalColor, pressedColor),
                ColorDrawable(normalColor),
                null
            )
        }

        fun roundedRippleDrawable(normalColor: Int, pressedColor: Int, cornerRadius: Float = dpf(15)): RippleDrawable {
            return RippleDrawable(
                getPressedColorSelector(normalColor, pressedColor),
                GradientDrawable().apply {
                    setColor(normalColor)
                    this.cornerRadius = cornerRadius
                },
                null,
            )
        }

        private fun getPressedColorSelector(normalColor: Int, pressedColor: Int): ColorStateList {
            return ColorStateList(
                arrayOf(
                    intArrayOf(R.attr.state_pressed),
                    intArrayOf(R.attr.state_focused),
                    intArrayOf(R.attr.state_activated),
                    intArrayOf()
                ), intArrayOf(
                    pressedColor,
                    pressedColor,
                    pressedColor,
                    normalColor
                )
            )
        }
    }
}