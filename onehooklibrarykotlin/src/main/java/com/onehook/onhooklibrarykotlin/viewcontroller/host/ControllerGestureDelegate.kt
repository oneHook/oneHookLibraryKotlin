package com.onehook.onhooklibrarykotlin.viewcontroller.host

import android.graphics.Point

interface ControllerGestureDelegate {
    fun isTouchOutside(point: Point): Boolean
}