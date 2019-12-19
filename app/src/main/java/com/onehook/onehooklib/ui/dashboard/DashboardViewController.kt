package com.onehook.onehooklib.ui.dashboard

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatEditText
import com.onehook.onehooklib.ui.reusable.BaseViewController
import com.onehook.onhooklibrarykotlin.utils.KeyboardObserver
import com.onehook.onhooklibrarykotlin.utils.dpToPx
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import io.reactivex.rxkotlin.addTo

class DashboardViewController : BaseViewController() {

    private val editText: AppCompatEditText by lazy {
        AppCompatEditText(context).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, dpToPx(100))
        }
    }

    private val keyboardObserver: KeyboardObserver by lazy {
        KeyboardObserver(activity).apply {
            keyboardVisible.subscribe {
                Log.d("oneHook", "XXX keyboard visible " + it)
            }.addTo(compositeDisposable = compositeDisposable)
        }
    }

    override fun viewDidLoad(view: View) {
        super.viewDidLoad(view)
        keyboardObserver
        (view as? ViewGroup)?.addView(editText)
    }
}