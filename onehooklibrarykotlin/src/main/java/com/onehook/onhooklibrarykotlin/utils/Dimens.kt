package com.onehook.onhooklibrarykotlin.utils

import android.content.res.Resources

val screenWidth: Int by lazy { Resources.getSystem().displayMetrics.widthPixels }
val screenHeight: Int by lazy { Resources.getSystem().displayMetrics.heightPixels }


fun dpf(dp: Int): Float {
    return dp * Resources.getSystem().displayMetrics.density
}

fun dp(dp: Int): Int {
    return dpf(dp).toInt()
}