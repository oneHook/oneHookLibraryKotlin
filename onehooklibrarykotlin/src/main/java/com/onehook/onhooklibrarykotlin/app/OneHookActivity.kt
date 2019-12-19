package com.onehook.onhooklibrarykotlin.app

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.onehook.onhooklibrarykotlin.viewcontroller.window.Window

open class OneHookActivity : AppCompatActivity() {

    open var disableBackButton: Boolean = false
    lateinit var view: FrameLayout
        private set

    val controllerWindow: Window by lazy {
        val window = Window(activity = this)
        view.addView(window)
        window
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        view = FrameLayout(this)
        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        controllerWindow.onDestroy()
    }

    override fun onBackPressed() {
        if (disableBackButton) {
            return
        }
        if (!controllerWindow.onBackPressed()) {
            super.onBackPressed()
        }
    }
}