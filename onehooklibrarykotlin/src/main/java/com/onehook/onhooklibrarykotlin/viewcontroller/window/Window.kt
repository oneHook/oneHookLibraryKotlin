package com.onehook.onhooklibrarykotlin.viewcontroller.window

import com.onehook.onhooklibrarykotlin.app.OHActivity
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.ViewController
import com.onehook.onhooklibrarykotlin.viewcontroller.host.ControllerHost
import com.onehook.onhooklibrarykotlin.viewcontroller.presentation.PresentingAnimation

class Window(activity: OHActivity) : ControllerHost(activity = activity) {

    fun setRootViewController(
        controller: ViewController
    ) {
        if (activity == null) {
            return
        }
        if (topViewController == null) {
            push(
                viewController = controller,
                activity = activity!!,
                animated = false,
                completion = null
            )
        } else if (topViewController?.tag != controller.tag) {
            popAllButTop()
            topViewController?.presentationStyle?.animation = PresentingAnimation.BOTTOM
            addBottom(viewController = controller, activity = activity!!)
            pop(animated = true, completion = null)
        }
    }
}