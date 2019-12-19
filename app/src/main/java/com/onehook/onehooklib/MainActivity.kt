package com.onehook.onehooklib

import android.graphics.Color
import android.os.Bundle
import com.onehook.onehooklib.ui.dashboard.DashboardViewController
import com.onehook.onhooklibrarykotlin.app.OHActivity
import com.onehook.onhooklibrarykotlin.utils.KeyboardObserver

class MainActivity : OHActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view.setBackgroundColor(Color.GRAY)
        setRootViewController(DashboardViewController())
    }
}
