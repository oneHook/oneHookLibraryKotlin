package com.onehook.onhooklibrarykotlin.viewcontroller.host

import android.graphics.Point

interface ControllerInteractiveGestureDelegate {
    fun isTouchOutside(point: Point): Boolean
}