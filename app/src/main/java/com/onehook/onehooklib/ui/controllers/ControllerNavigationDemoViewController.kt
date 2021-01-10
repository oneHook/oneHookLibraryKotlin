package com.onehook.onehooklib.ui.controllers

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onehook.onehooklib.ui.reusable.BaseViewController
import com.onehook.onehooklib.ui.reusable.RoundedSolidButton
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.utils.dpf
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.EDView
import com.onehook.onhooklibrarykotlin.viewcontroller.host.PresentationStyle
import com.onehook.onhooklibrarykotlin.viewcontroller.host.transition.BottomToTopControllerTransition
import com.onehook.onhooklibrarykotlin.viewcontroller.host.transition.LeftToRightControllerTransition
import com.onehook.onhooklibrarykotlin.widget.StackLayout

private class SimpleDemoViewController : BaseViewController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return EDView(this).apply {
            setBackgroundColor(Color.BLUE)
        }
    }
}

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
                    present(viewController = controller, animated = true)
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
                    present(viewController = controller, animated = true)
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
                    present(viewController = controller, animated = true)
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return contentView
    }
}