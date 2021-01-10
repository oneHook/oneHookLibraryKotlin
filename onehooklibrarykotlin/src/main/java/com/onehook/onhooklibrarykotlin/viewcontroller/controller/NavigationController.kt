package com.onehook.onhooklibrarykotlin.viewcontroller.controller

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onehook.onhooklibrarykotlin.viewcontroller.host.ControllerHost
import com.onehook.onhooklibrarykotlin.viewcontroller.host.transition.LeftToRightControllerTransition

open class NavigationController(var root: ViewController) : ViewController() {

    /**
     * Gonna use another controller host for all the sub-view controllers.
     */
    private var controllerHost: ControllerHost? = null
    private var pendingRoot: ViewController? = root

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return ControllerHost(activity = activity).also {
            controllerHost = it
            it.setBackgroundColor(Color.BLACK)
        }
    }

    override fun viewWillAppear(animated: Boolean) {
        super.viewWillAppear(animated)

        if (pendingRoot != null) {
            pendingRoot?.navigationController = this
            controllerHost?.push(
                viewController = pendingRoot!!,
                activity = activity,
                animated = false,
                completion = null
            )
            pendingRoot = null
        }
    }

    override fun doDestroyView(container: ViewGroup) {
        /* Make sure everything is poped */
        controllerHost?.popAll()
        controllerHost?.onDestroy()
        root.navigationController = null
        super.doDestroyView(container)
    }

    /**
     * Push view controller to the stack.
     */
    fun push(viewController: ViewController, animated: Boolean, completion: (() -> Unit)? = null) {
        view.post {
            viewController.navigationController = this
            viewController.presentationStyle.transition = viewController.presentationStyle.transition ?: LeftToRightControllerTransition()
            controllerHost?.push(
                viewController = viewController,
                activity = activity,
                animated = animated,
                completion = completion
            )
        }
    }

    /**
     * Pop the most recent view controller.
     * No effect if only one view controller in stack.
     */
    fun popViewController(animated: Boolean, completion: (() -> Unit)? = null) {
        controllerHost?.pop(animated, completion = completion)
    }

    fun popToRoot() {
        controllerHost?.popToRoot()
    }

    override fun onBackPressed(): Boolean {
        return controllerHost?.onBackPressed() ?: false
    }
}