package com.onehook.onhooklibrarykotlin.viewcontroller.host

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import com.onehook.onhooklibrarykotlin.app.OHActivity
import com.onehook.onhooklibrarykotlin.utils.AnimationEndListener
import com.onehook.onhooklibrarykotlin.utils.weak
import com.onehook.onhooklibrarykotlin.view.EXACTLY
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.NavigationController
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.ViewController
import com.onehook.onhooklibrarykotlin.viewcontroller.host.transition.BottomToTopControllerTransition

open class ControllerHost(activity: OHActivity) : FrameLayout(activity) {

    private var dimCovers = ArrayList<DimView>()
    private var controllers = ArrayList<ViewController>()

    var activity: OHActivity? by weak()
        private set

    init {
        this.activity = activity
        setBackgroundColor(Color.TRANSPARENT)
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    fun onDestroy() {
        popAll()
        activity = null
    }

    val topViewController: ViewController?
        get() = controllers.lastOrNull()

    @Suppress
    val viewControllers: List<ViewController>
        get() = controllers

    /**
     * True if there is a transaction in progress, push/pop/etc will not work until
     * transaction is done.
     */
    var transactionInProgress: Boolean = false

    /**
     * Add to the bottom of view controller stack. NO animation.
     */
    fun addBottom(viewController: ViewController, activity: OHActivity) {
        if (viewControllers.any { it.tag == viewController.tag }) {
            /* only allow unique tag */
            return
        }
        viewController.doCreateView(container = this, activity = activity)
        viewController.also {
            it.view.layoutParams = LayoutParams(viewController.view.layoutParams).apply {
                width = LayoutParams.MATCH_PARENT
                height = LayoutParams.MATCH_PARENT
            }
            controllers.add(0, it)
            addView(it.view, 0)
            measureChild(viewController.view)
            it.view.visibility = View.GONE
        }
        DimView(context = context).also { cover ->
            dimCovers.add(0, cover)
            addView(cover, 0)
        }
        printStack()
    }

    /**
     * Push a new view controller to the top of controller stack.
     */
    fun push(
        viewController: ViewController,
        activity: OHActivity,
        animated: Boolean,
        completion: (() -> Unit)?
    ) {
        if (transactionInProgress) {
            return
        }
        if (viewControllers.any { it.tag == viewController.tag }) {
            /* only allow unique tag */
            return
        }
        printStack()
        transactionInProgress = true
        viewController.doCreateView(container = this, activity = activity)
        viewController.view.layoutParams = LayoutParams(viewController.view.layoutParams)
        val currentTop = topViewController
        val cover = DimView(context = context).also { cover ->
            dimCovers.add(cover)
            addView(cover)
        }
        addView(viewController.view)
        measureChild(viewController.view)
        viewController.viewWillAppear(animated)
        if (!viewController.presentationStyle.overCurrentContext) {
            currentTop?.viewWillDisappear(animated)
        }

        val transition =
            viewController.presentationStyle.transition ?: BottomToTopControllerTransition()
        val transitionContext = ControllerTransition.TransitionContext(
            fromController = currentTop,
            toController = viewController,
            cover = cover,
            frame = Rect(left, top, right, bottom)
        )
        if (animated) {
            transition.createEnteringAnimation(transitionContext).apply {
                addListener(object : AnimationEndListener() {
                    override fun onAnimationEnd(animation: Animator?) {
                        bringChildToFront(cover)
                        bringChildToFront(viewController.view)
                        viewController.viewDidAppear(animated)
                        if (!viewController.presentationStyle.overCurrentContext) {
                            currentTop?.viewDidDisappear(animated)
                            currentTop?.view?.visibility = View.GONE
                        }
                        transactionInProgress = false
                        transition.onEnteringAnimationFinished(transitionContext)
                        printStack()
                        completion?.invoke()
                    }
                })
                start()
            }
        } else {
            transition.onEnteringAnimationFinished(transitionContext)
            viewController.viewDidAppear(animated)
            if (!viewController.presentationStyle.overCurrentContext) {
                currentTop?.viewDidDisappear(animated)
            }
            transactionInProgress = false
            printStack()
            completion?.invoke()
        }
        controllers.add(viewController)
    }

    /**
     * Pop every single controllers in the stack. NO ANIMATION.
     */
    fun popAll() {
        topViewController?.viewWillDisappear(animated = false)
        topViewController?.viewDidDisappear(animated = false)
        controllers.reversed().forEach {
            it.navigationController = null
            it.doDestroyView(this)
        }
        dimCovers.reversed().forEach {
            removeView(it)
        }
        controllers.clear()
        dimCovers.clear()
    }

    /**
     * Pop to root view controller. NO ANIMATION.
     */
    fun popToRoot() {
        while (controllers.size > 1) {
            pop(animated = false, completion = null)
        }
        printStack()
    }

    /**
     * Pop all view controller except the top one. NO ANIMATION.
     */
    fun popAllButTop() {
        while (controllers.size > 1) {
            val index = controllers.size - 2
            val toPop = controllers.removeAt(index)
            if (toPop.isVisible) {
                toPop.viewWillDisappear(animated = false)
                toPop.viewDidDisappear(animated = false)
            }
            toPop.doDestroyView(this)
            val coverToRemove = dimCovers.removeAt(index)
            removeView(coverToRemove)
        }
        printStack()
    }

    /**
     * Pop the top view controller if there is more than one view controller in stack.
     */
    fun pop(animated: Boolean, completion: (() -> Unit)?): Boolean {
        printStack()
        if (transactionInProgress) {
            return false
        }
        if (controllers.size == 1) {
            return false
        }
        transactionInProgress = true
        val toPop = controllers.removeAt(controllers.size - 1)
        val cover = dimCovers.removeAt(dimCovers.size - 1)
        val newTop = topViewController
        newTop?.view?.visibility = View.VISIBLE
        if (!toPop.presentationStyle.overCurrentContext) {
            newTop?.viewWillAppear(animated)
        }
        if (toPop.isVisible) {
            toPop.viewWillDisappear(animated)
        }

        val transition =
            toPop.presentationStyle.transition ?: BottomToTopControllerTransition()
        val transitionContext = ControllerTransition.TransitionContext(
            fromController = newTop,
            toController = toPop,
            cover = cover,
            frame = Rect(left, top, right, bottom)
        )

        if (animated) {
            transition.createExitingAnimation(transitionContext).apply {
                addListener(object : AnimationEndListener() {
                    override fun onAnimationEnd(animation: Animator?) {
                        transition.onExitingAnimationFinished(transitionContext)
                        removeView(cover)
                        if (newTop != null) {
                            bringChildToFront(newTop.view)
                        }
                        if (!toPop.presentationStyle.overCurrentContext) {
                            newTop?.viewDidAppear(animated)
                        }
                        if (toPop.isVisible) {
                            toPop.viewDidDisappear(animated)
                        }
                        toPop.doDestroyView(this@ControllerHost)
                        transactionInProgress = false
                        printStack()
                        completion?.invoke()
                    }
                })
                start()
            }
        } else {
            if (!toPop.presentationStyle.overCurrentContext) {
                newTop?.viewDidAppear(animated = animated)
            }
            transition.onExitingAnimationFinished(transitionContext)
            if (toPop.isVisible) {
                toPop.viewDidDisappear(animated)
            }
            removeView(cover)
            toPop.doDestroyView(this@ControllerHost)
            transactionInProgress = false
            printStack()
            completion?.invoke()
        }
        return true
    }

    open fun onBackPressed(): Boolean {
        if (topViewController?.onBackPressed() == true) {
            return true
        }
        if (topViewController?.presentationStyle?.allowDismissByBackButton == false) {
            return true
        }
        if (controllers.size <= 1) {
            return false
        }
        pop(animated = true, completion = null)
        return true
    }

    private fun measureChild(view: View) {
        view.measure(EXACTLY(measuredWidth), EXACTLY(measuredHeight))
    }

    private fun printStack() {
        log("===========")
        log("${controllers.size} In Stack, ${dimCovers.size} covers and ${childCount} subViews")
        log("Stack     :", controllers)
        log("Dim Covers:", dimCovers)
        log("===========")
    }

    private var testRect = Rect()

    private val detector =
        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onDown(e: MotionEvent?): Boolean {
                println("XXX on down")
                return true
            }

            //            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
//                println("XXX single tap confirmed")
//                return true
//            }
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                println("XXX single tap up")
                if (e == null) {
                    return false
                }
                topViewController?.also { currentTop ->
                    if (currentTop.presentationStyle.allowDismissByTapOutside &&
                        (currentTop as? ControllerGestureDelegate)?.isTouchOutside(
                            Point(
                                e.x.toInt(),
                                e.y.toInt()
                            )
                        ) == true
                    ) {
                        pop(animated = true, completion = null)
                    }
                }
                return true
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                println("XXX on scroll ${distanceX} ${distanceY}")
                return true
            }
        })

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (childCount == 0 || ev == null) {
            return false
        }
        /* Always dispatch to the top view controller */
        val currentTop = topViewController
        currentTop?.view?.dispatchTouchEvent(ev)
        if (currentTop is NavigationController) {
            return true
        }
        detector.onTouchEvent(ev)
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> println("XXX action down")
            MotionEvent.ACTION_MOVE -> println("XXX action move")
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> println("XXX action up/cancel")
        }
        return true
    }
}

/**
 * Cover view in between view controllers.
 */
private class DimView(context: Context) : View(context) {

    init {
        layoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        setBackgroundColor(Color.BLACK)
        alpha = 0f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }
}

private fun log(vararg args: Any) {
    Log.d("oneHook XXX", args.joinToString(" "))
}