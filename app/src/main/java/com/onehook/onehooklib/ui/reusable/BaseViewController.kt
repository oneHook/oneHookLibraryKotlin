package com.onehook.onehooklib.ui.reusable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.ViewController
import io.reactivex.disposables.CompositeDisposable

open class BaseViewController : ViewController() {

    protected var compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return FrameLayout(container.context)
    }

    override fun viewWillUnload(view: View) {
        super.viewWillUnload(view)
        compositeDisposable.clear()
    }
}