package com.onehook.onehooklib.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.marginTop
import com.onehook.onehooklib.ui.reusable.BaseViewController
import com.onehook.onhooklibrarykotlin.utils.KeyboardObserver
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.view.EXACTLY
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import com.onehook.onhooklibrarykotlin.view.WRAP_CONTENT
import com.onehook.onhooklibrarykotlin.view.setFrame
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.ViewController
import io.reactivex.rxkotlin.addTo

object lp {
    fun frameLayoutLp(): FrameLayout.LayoutParams {
        return FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }
    fun LinearLayoutLp(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }
}

fun View.layout(x: Int, y: Int, width: Int, height: Int) {
    layout(x, y, x + width, y + height)
}



class DashboardViewController : BaseViewController() {

    private val editText: AppCompatEditText by lazy {
        AppCompatEditText(context).apply {
            setBackgroundColor(Color.CYAN)
            layoutParams = lp.frameLayoutLp().apply {
                width = MATCH_PARENT
                height = dp(100)
                setMargins(0, activity.safeArea.top, 0, activity.safeArea.bottom)
            }
        }
    }

    private val bottomButton: AppCompatButton by lazy {
        AppCompatButton(context).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, dp(50)).apply {
                setBackgroundColor(Color.CYAN)
                gravity = Gravity.BOTTOM or Gravity.CENTER
                setMargins(dp(20), 0, dp(20), activity.safeArea.bottom)
            }
            setOnClickListener {
                Log.d("XXX", "Clicked!")
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
        view.setBackgroundColor(Color.WHITE)
        keyboardObserver
        (view as? ViewGroup)?.apply {
            addView(editText)
            addView(bottomButton)
        }
    }

    override fun viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        Log.d("XXX", "" + view.width + "," + view.height)
        Log.d("XXX", "" + activity.safeArea.top + "," + activity.safeArea.bottom)
        bottomButton.measure(EXACTLY(dp(200)), EXACTLY(dp(50)))
        bottomButton.layout(x=dp(10), y=dp(50), width=dp(200), height=dp(50))
    }
}