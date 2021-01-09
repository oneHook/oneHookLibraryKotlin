package com.onehook.onehooklib.ui.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onehook.onehooklib.R
import com.onehook.onehooklib.ui.reusable.BaseViewController

class StackLayoutDemoViewController : BaseViewController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_stack_layout_demo, container, false).apply {
            setPadding(0, activity.safeArea.top, 0, 0)
        }
    }
}