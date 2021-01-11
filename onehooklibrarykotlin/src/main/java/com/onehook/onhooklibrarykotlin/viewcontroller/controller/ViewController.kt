package com.onehook.onhooklibrarykotlin.viewcontroller.controller

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onehook.onhooklibrarykotlin.app.OHActivity
import com.onehook.onhooklibrarykotlin.utils.weak
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import com.onehook.onhooklibrarykotlin.view.ViewRes
import com.onehook.onhooklibrarykotlin.viewcontroller.host.ControllerInteractiveGestureDelegate
import com.onehook.onhooklibrarykotlin.viewcontroller.host.PresentationStyle
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

open class ViewController {

    companion object {
        var verbose: Boolean = true
    }

    var navigationController: NavigationController? by weak()
    var interactiveGestureDelegate: ControllerInteractiveGestureDelegate? = null

    private var _view: View? = null
    val view: View
        get() = _view!!

    private var _activity: OHActivity? by weak()
    val activity: OHActivity
        get() = _activity!!
    val context: Context
        get() = _activity!!

    private var _isVisible: Boolean = false
    val isVisible: Boolean
        get() = _isVisible

    var presentationStyle = PresentationStyle()
    var tag: String = javaClass.name

    val res: Resources
        get() = _activity!!.resources

    /* Child must override */

    open fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return EDView(this)
    }

    open fun viewDidLoad(view: View) {
        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) VIEW DID LOAD")
        }
        processViewResAnnotations()
    }

    private fun processViewResAnnotations() {
        for (field in this::class.memberProperties) {
            if (field is KMutableProperty<*>) {
                field.findAnnotation<ViewRes>()?.let {
                    field.isAccessible = true
                    field.setter.call(this, this.view.findViewById(it.res))
                }
            }
        }
    }

    open fun viewWillUnload(view: View) {
        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) VIEW WILL UNLOAD")
        }
    }

    open fun viewWillAppear(animated: Boolean) {
        if (verbose) {
            Log.i(
                "oneHook",
                "${javaClass.simpleName} ($tag) VIEW WILL APPEAR (${view.measuredWidth}, ${view.measuredHeight})"
            )
        }
    }

    open fun viewDidAppear(animated: Boolean) {
        _isVisible = true
        if (verbose) {
            Log.i(
                "oneHook",
                "${javaClass.simpleName} ($tag) VIEW DID APPEAR (${view.measuredWidth}, ${view.measuredHeight})"
            )
        }
    }

    open fun viewWillDisappear(animated: Boolean) {
        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) VIEW WILL DISAPPEAR")
        }
    }

    open fun viewDidDisappear(animated: Boolean) {
        _isVisible = false
        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) VIEW DID DISAPPEAR")
        }
    }

    open fun viewWillLayoutSubviews() {
        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) VIEW WILL LAYOUT SUBVIEWS")
        }
    }

    open fun viewDidLayoutSubviews() {
        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) VIEW DID LAYOUT SUBVIEWS")
        }
    }

    /* Internal Methods */

    open fun doCreateView(container: ViewGroup, activity: OHActivity) {
        _activity = activity
        _view = onCreateView(LayoutInflater.from(container.context), container)
        if (_view?.layoutParams == null) {
            _view?.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        viewDidLoad(view)
        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) CREATE VIEW")
        }
    }

    open fun doDestroyView(container: ViewGroup) {
        this.viewWillUnload(view)
        container.removeView(view)
        _activity = null
        _view = null
        navigationController = null

        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) DESTROY VIEW")
        }
    }

    /**
     * If return true, means view controller would like to handle this
     * by itself.
     */
    open fun onBackPressed(): Boolean {
        return false
    }

    /* Public methods */

    open fun present(
        viewController: ViewController,
        animated: Boolean,
        completion: (() -> Unit)? = null
    ) {
        activity.controllerWindow.push(
            viewController = viewController,
            activity = activity,
            animated = animated,
            completion = completion
        )
    }

    open fun dismiss(animated: Boolean, completion: (() -> Unit)? = null) {
        activity.controllerWindow.pop(animated = animated, completion = completion)
    }
}

/**
 * ViewGroup subclass that works with ViewController to notify
 * controller when layout is needed.
 */
class EDView(private val viewController: ViewController) : ViewGroup(viewController.activity) {
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        viewController.viewWillLayoutSubviews()
        viewController.viewDidLayoutSubviews()
    }
}