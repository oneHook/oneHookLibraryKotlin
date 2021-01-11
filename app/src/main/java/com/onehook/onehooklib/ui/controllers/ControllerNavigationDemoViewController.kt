package com.onehook.onehooklib.ui.controllers

import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.graphics.contains
import com.onehook.onehooklib.ui.reusable.BaseViewController
import com.onehook.onehooklib.ui.reusable.LargeTextView
import com.onehook.onehooklib.ui.reusable.RoundedSolidButton
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.utils.dpf
import com.onehook.onhooklibrarykotlin.view.LP
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.EDView
import com.onehook.onhooklibrarykotlin.viewcontroller.host.ControllerInteractiveGestureDelegate
import com.onehook.onhooklibrarykotlin.viewcontroller.host.PresentationStyle
import com.onehook.onhooklibrarykotlin.viewcontroller.host.transition.BottomToTopControllerTransition
import com.onehook.onhooklibrarykotlin.viewcontroller.host.transition.FadeControllerTransition
import com.onehook.onhooklibrarykotlin.viewcontroller.host.transition.LeftToRightControllerTransition
import com.onehook.onhooklibrarykotlin.widget.StackLayout

class ControllerNavigationDemoViewController : BaseViewController() {
    private val contentView: ViewGroup by lazy {
        StackLayout(context).apply {
            setBackgroundColor(Color.WHITE)
            orientation = StackLayout.Orientation.VERTICAL
            spacing = dpf(10)
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "BOTTOM UP DEFAULT"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle =
                            PresentationStyle(transition = BottomToTopControllerTransition())
                    }
                    present(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "BOTTOM UP NO DIM"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle =
                            PresentationStyle(transition = BottomToTopControllerTransition().apply {
                                dim = false
                            })
                    }
                    present(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "BOTTOM UP NO MOVE"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle =
                            PresentationStyle(transition = BottomToTopControllerTransition().apply {
                                dismissMoveRatio = 0f
                            })
                    }
                    present(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "LEFT RIGHT DEFAULT"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle =
                            PresentationStyle(transition = LeftToRightControllerTransition())
                    }
                    navigationController?.push(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "LEFT RIGHT NO DIML"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle =
                            PresentationStyle(transition = LeftToRightControllerTransition().apply {
                                dim = false
                            })
                    }
                    navigationController?.push(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "LEFT RIGHT NO MOVE"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle =
                            PresentationStyle(transition = LeftToRightControllerTransition().apply {
                                dismissMoveRatio = 0f
                            })
                    }
                    navigationController?.push(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "Fade"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle =
                            PresentationStyle(transition = FadeControllerTransition().apply {
                                shouldFadeoutFrom = true
                            })
                    }
                    present(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "Dialog"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDialogViewControllerInteractive().apply {
                        presentationStyle =
                            PresentationStyle(
                                overCurrentContext = true,
                                transition = BottomToTopControllerTransition().also {
                                    it.dismissMoveRatio = 0f
                                    it.dimRatio = 0.5f
                                }
                            )
                    }
                    present(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "Overlay"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleOverlayViewControllerInteractive().apply {
                        presentationStyle =
                            PresentationStyle(
                                overCurrentContext = true,
                                transition = BottomToTopControllerTransition().also {
                                    it.dimRatio = 0.5f
                                }
                            )
                    }
                    present(viewController = controller, animated = true)
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return contentView
    }
}

private class SimpleDemoViewController : BaseViewController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return EDView(this).apply {
            setBackgroundColor(Color.BLUE)
        }
    }
}

private class SimpleDialogViewControllerInteractive : BaseViewController() {

    private lateinit var container: View
    private val content: FrameLayout by lazy {
        FrameLayout(context).apply {
            setBackgroundColor(Color.TRANSPARENT)
            addView(LargeTextView(context = context).apply {
                container = this
                layoutParams = LP().apply {
                    width = dp(200)
                    height = dp(150)
                    layoutGravity = Gravity.CENTER
                }.frameLayoutLp()
                text = "I am a dialog"
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setBackgroundColor(Color.BLUE)
            })
            addView(RoundedSolidButton(context = context, color = Color.YELLOW).apply {
                layoutParams = LP().apply {
                    layoutGravity = Gravity.CENTER
                }.frameLayoutLp()
                setPadding(dp(20), dp(10), dp(20), dp(10))
                text = "Click Me"
            })
        }
    }

    init {
        interactiveGestureDelegate = object : ControllerInteractiveGestureDelegate {
            override fun isTouchOutside(point: Point): Boolean {
                val rect = Rect()
                container.getHitRect(rect)
                return !rect.contains(point)
            }

            override fun canStartInteractiveDismiss(point: Point): Boolean = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return content
    }
}

private class SimpleOverlayViewControllerInteractive : BaseViewController() {

    private lateinit var container: View
    private val content: FrameLayout by lazy {
        FrameLayout(context).apply {
            setBackgroundColor(Color.TRANSPARENT)
            addView(LargeTextView(context = context).apply {
                container = this
                layoutParams = LP().apply {
                    width = MATCH_PARENT
                    height = MATCH_PARENT
                    topMargin = dp(150)
                    layoutGravity = Gravity.CENTER
                }.frameLayoutLp()
                text = "I am a overlay"
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setBackgroundColor(Color.BLUE)
            })
        }
    }

    init {
        interactiveGestureDelegate = object : ControllerInteractiveGestureDelegate {
            override fun isTouchOutside(point: Point): Boolean {
                val rect = Rect()
                container.getHitRect(rect)
                return !rect.contains(point)
            }

            override fun canStartInteractiveDismiss(point: Point): Boolean {
                return point.y < dp(150)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return content
    }
}