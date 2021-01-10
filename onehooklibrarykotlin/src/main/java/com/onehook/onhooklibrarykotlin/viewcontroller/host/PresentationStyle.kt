package com.onehook.onhooklibrarykotlin.viewcontroller.host

data class PresentationStyle(
    var overCurrentContext: Boolean = false,
    var allowDismiss: Boolean = true,
    var allowTapOutsideToDismiss: Boolean = true,
    var transition: ControllerTransition? = null
)