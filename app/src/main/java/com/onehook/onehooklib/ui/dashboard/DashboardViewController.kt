package com.onehook.onehooklib.ui.dashboard

import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.onehook.onehooklib.ui.reusable.BaseViewController
import com.onehook.onhooklibrarykotlin.utils.KeyboardObserver
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import io.reactivex.rxkotlin.addTo

class DashboardViewController : BaseViewController() {

    private val editText: AppCompatEditText by lazy {
        AppCompatEditText(context).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, dp(100))
        }
    }

    private val bottomButton: AppCompatButton by lazy {
        AppCompatButton(context).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, dp(50)).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER
                marginStart = dp(20)
                marginEnd = dp(20)
            }
            text = "Confirm"
        }
    }

    private val keyboardObserver: KeyboardObserver by lazy {
        KeyboardObserver(activity).apply {
            keyboardOffset.subscribe {
                bottomButton.animate()
                    .translationY(-it.toFloat() - bottomButton.measuredHeight)
                    .setDuration(150).start()
            }.addTo(compositeDisposable = compositeDisposable)
            keyboardVisible.subscribe {
                Log.d("oneHook", "XXX keyboard visible " + it + " , " + keyboardHeight.value)
            }.addTo(compositeDisposable = compositeDisposable)
        }
    }

    override fun viewDidLoad(view: View) {
        super.viewDidLoad(view)
        view.setPadding(dp(24), dp(24), dp(24), dp(24))
        keyboardObserver
        (view as? ViewGroup)?.addView(editText)
        (view as? ViewGroup)?.addView(bottomButton)
    }
}