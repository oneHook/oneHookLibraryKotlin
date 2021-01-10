package com.onehook.onhooklibrarykotlin.viewcontroller.host

data class PresentationStyle(
    /**
     * determine if presenting view controller's view should stay visible.
     */
    var overCurrentContext: Boolean = false,

    /**
     * If presented controller can be dismissed by back button.
     */
    var allowDismissByBackButton: Boolean = true,

    /**
     * If presented controller can be dismissed by tapping outside.
     */
    var allowDismissByTapOutside: Boolean = true,

    /**
     * Transition object to determine the animation. will use
     * @see com.onehook.onhooklibrarykotlin.viewcontroller.host.transition.BottomToTopControllerTransition
     */
    var transition: ControllerTransition? = null
)