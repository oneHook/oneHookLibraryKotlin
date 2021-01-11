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
            if (it.presentationStyle.transition == null) {
                it.presentationStyle.transition = BottomToTopControllerTransition()
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
        if (viewController.presentationStyle.transition == null) {
            viewController.presentationStyle.transition = BottomToTopControllerTransition()
        }
        val transition = viewController.presentationStyle.transition!!
        val transitionContext = ControllerTransition.TransitionContext(
            fromController = currentTop,
            toController = viewController,
            cover = cover,
            frame = Rect(left, top, right, bottom)
        )
        if (animated) {
            transition.onExitingAnimationFinished(transitionContext)
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

    private val detector =
        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean = handleInteractiveGesture
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                if (e == null) {
                    return false
                }
                tempPoint.x = e.x.toInt()
                tempPoint.y = e.y.toInt()
                topViewController?.also { currentTop ->
                    if (currentTop.presentationStyle.allowDismissByTapOutside &&
                        currentTop.interactiveGestureDelegate?.isTouchOutside(point = tempPoint) == true
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
                if (e1 == null || e2 == null) {
                    return false
                }
                startPoint.x = e1.x.toInt()
                startPoint.y = e1.y.toInt()
                currentPoint.x = e2.x.toInt()
                currentPoint.y = e2.y.toInt()

                if (interactiveDismissContext == null) {
                    startInteractiveDismiss()
                } else {
                    onInteractiveDismiss()
                }
                return true
            }
        })

    private var tempPoint = Point()
    private var startPoint = Point()
    private var currentPoint = Point()
    private var handleInteractiveGesture: Boolean = false
    private var interactiveDismissContext: ControllerTransition.TransitionContext? = null

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (childCount == 0 || ev == null) {
            return false
        }

        /* Always dispatch to the top view controller */
        val currentTop = topViewController
        currentTop?.view?.dispatchTouchEvent(ev)
        /* if top is navigation controller, let it
           handle interactive gesture
         */
        if (currentTop is NavigationController) {
            return true
        }

        tempPoint.x = ev.x.toInt()
        tempPoint.y = ev.y.toInt()

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                val isTouchingOutside =
                    currentTop?.interactiveGestureDelegate?.isTouchOutside(point = tempPoint) == true
                val canStartInteractiveDismiss =
                    controllers.size > 1 &&
                            currentTop?.interactiveGestureDelegate?.canStartInteractiveDismiss(point = tempPoint) == true
                handleInteractiveGesture = isTouchingOutside || canStartInteractiveDismiss
                if (handleInteractiveGesture) {
                    detector.onTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (handleInteractiveGesture) {
                    detector.onTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (handleInteractiveGesture) {
                    detector.onTouchEvent(ev)
                }
                if (interactiveDismissContext != null) {
                    endInteractiveDismiss()
                }
                handleInteractiveGesture = false
            }
        }
        return true
    }

    private fun startInteractiveDismiss() {
        if (controllers.size <= 1) {
            return
        }
        val fromController = controllers[controllers.size - 2].apply {
            view.visibility = View.VISIBLE
        }
        val cover = dimCovers[dimCovers.size - 1]
        val toController = controllers[controllers.size - 1]

        ControllerTransition.TransitionContext(
            fromController = fromController,
            toController = toController,
            cover = cover,
            frame = Rect(left, top, right, bottom)
        ).also {
            interactiveDismissContext = it
            toController.presentationStyle.transition?.updateInteractiveDismiss(
                context = it,
                start = startPoint,
                current = currentPoint
            )
        }

    }

    private fun onInteractiveDismiss() {
        interactiveDismissContext?.also { context ->
            context.toController.presentationStyle.transition?.updateInteractiveDismiss(
                context = context,
                start = startPoint,
                current = currentPoint
            )
        }
    }

    private fun endInteractiveDismiss() {
        val fromController = controllers[controllers.size - 2]
        val toController = controllers[controllers.size - 1]

        interactiveDismissContext?.also { context ->
            val shouldDismiss =
                context.toController.presentationStyle.transition?.finishInteractiveDismiss(
                    context = context,
                    start = startPoint,
                    current = currentPoint
                ) == true

            if (shouldDismiss) {
                pop(animated = true, completion = null)
            } else {
                toController.presentationStyle.transition?.createEnteringAnimation(context)?.apply {
                    addListener(object : AnimationEndListener() {
                        override fun onAnimationEnd(animation: Animator?) {
                            if (toController.presentationStyle.overCurrentContext.not()) {
                                fromController.view.visibility = View.GONE
                            }
                        }
                    })
                    start()
                }
            }
        }
        interactiveDismissContext = null
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
    Log.d("oneHook", args.joinToString(" "))
}