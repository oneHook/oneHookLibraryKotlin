package com.onehook.onhooklibrarykotlin.utils

import android.content.res.Resources

val screenWidth: Int by lazy { Resources.getSystem().displayMetrics.widthPixels }
val screenHeight: Int by lazy { Resources.getSystem().displayMetrics.heightPixels }

fun dp(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}