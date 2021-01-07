package com.onehook.onehooklib

import android.graphics.Color
import android.os.Bundle
import com.onehook.onehooklib.ui.dashboard.MenuViewController
import com.onehook.onhooklibrarykotlin.app.OHActivity

class MainActivity : OHActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onWindowReady() {
        super.onWindowReady()
        view.setBackgroundColor(Color.WHITE)
        setRootViewController(MenuViewController())
    }
}
