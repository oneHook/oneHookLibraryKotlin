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
import com.onehook.onhooklibrarykotlin.viewcontroller.presentation.PresentationStyle
import com.onehook.onhooklibrarykotlin.viewcontroller.presentation.PresentingAnimation
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
                text = "BOTTOM"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle = PresentationStyle(animation = PresentingAnimation.BOTTOM)
                    }
                    present(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "BOTTOM FADE"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle = PresentationStyle(animation = PresentingAnimation.BOTTOM_FADE)
                    }
                    present(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "FADE"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle = PresentationStyle(animation = PresentingAnimation.FADE)
                    }
                    present(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "RIGHT"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle = PresentationStyle(animation = PresentingAnimation.RIGHT)
                    }
                    present(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "RIGHT REVEAL"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle = PresentationStyle(animation = PresentingAnimation.RIGHT_REVEAL)
                    }
                    present(viewController = controller, animated = true)
                }
            })
            addView(RoundedSolidButton(context = context, color = Color.RED).apply {
                text = "RIGHT TRANSLATION"
                setPadding(dp(20), dp(5), dp(20), dp(5))
                setOnClickListener {
                    val controller = SimpleDemoViewController().apply {
                        presentationStyle = PresentationStyle(animation = PresentingAnimation.RIGHT_TRANSLATION)
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