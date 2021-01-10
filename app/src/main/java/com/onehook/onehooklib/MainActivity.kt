package com.onehook.onehooklib

import android.graphics.Color
import com.onehook.onehooklib.ui.dashboard.MenuViewController
import com.onehook.onhooklibrarykotlin.app.OHActivity
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.NavigationController

class MainActivity : OHActivity() {

    override fun onWindowReady() {
        super.onWindowReady()
        view.setBackgroundColor(Color.WHITE)
        val navigationController = NavigationController(root = MenuViewController())
        setRootViewController(navigationController)
    }
}
