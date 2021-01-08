package com.onehook.onehooklib.ui.reusable

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView

open class LargeTextView(context: Context): AppCompatTextView(context) {
    init {
        textSize = 14.0f
    }
}

open class MediumTextView(context: Context): AppCompatTextView(context) {
    init {
        textSize = 12.0f
    }
}

open class SmallTextView(context: Context): AppCompatTextView(context) {
    init {
        textSize = 10.0f
    }
}