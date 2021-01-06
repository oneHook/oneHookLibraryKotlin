package com.onehook.onehooklib.ui.reusable

import android.view.View
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.ViewController
import io.reactivex.disposables.CompositeDisposable

open class BaseViewController : ViewController() {

    protected var compositeDisposable = CompositeDisposable()

    override fun viewWillUnload(view: View) {
        super.viewWillUnload(view)
        compositeDisposable.clear()
    }
}