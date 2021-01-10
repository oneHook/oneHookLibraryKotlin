package com.onehook.onhooklibrarykotlin.app

import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.ViewController
import com.onehook.onhooklibrarykotlin.viewcontroller.window.Window


open class OHActivity : AppCompatActivity() {

    var safeArea = Rect()
        private set
    var disableBackButton: Boolean = false

    private var isWindowReady: Boolean = false

    lateinit var view: CoordinatorLayout
        private set

    val controllerWindow: Window by lazy {
        val window = Window(activity = this)
        view.addView(window)
        window
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = CoordinatorLayout(this)
        setContentView(view)

        window.decorView.setOnApplyWindowInsetsListener { _, insets ->
            safeArea.top = insets.stableInsetTop
            safeArea.bottom = insets.stableInsetBottom
            if (isWindowReady.not()) {
                isWindowReady = true
                onWindowReady()
            }
            insets
        }
    }

    /**
     * Called when window is fully ready. SafeArea is set.
     */
    open fun onWindowReady() {

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

    fun setRootViewController(viewController: ViewController) {
        controllerWindow.setRootViewController(controller = viewController)
    }
}