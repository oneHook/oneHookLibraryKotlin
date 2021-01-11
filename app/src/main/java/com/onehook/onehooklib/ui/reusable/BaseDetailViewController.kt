package com.onehook.onehooklib.ui.reusable

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.utils.dpf
import com.onehook.onhooklibrarykotlin.view.LP
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.ViewController
import com.onehook.onhooklibrarykotlin.widget.Toolbar
import io.reactivex.disposables.CompositeDisposable

abstract class BaseDetailViewController : ViewController() {

    protected var compositeDisposable = CompositeDisposable()

    val toolbar: Toolbar by lazy {
        Toolbar(context = context).apply {
            layoutParams = LP().apply {
                width = MATCH_PARENT
                height = activity.safeArea.top + dp(56)
            }.frameLayoutLp()
            setPadding(0, activity.safeArea.top, 0, 0)
            title.getOrBuild().text = "oneHook Library"
            setBackgroundColor(Color.parseColor("#BBFFFFFF"))
            elevation = dpf(8)
        }
    }

    lateinit var _contentView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return FrameLayout(container.context).also {
            (it as? ViewGroup)?.apply {
                _contentView = onCreateContentView(inflater = inflater, container = container)
                addView(_contentView)
                addView(toolbar)
            }
        }
    }

    abstract fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View

    override fun viewWillUnload(view: View) {
        super.viewWillUnload(view)
        compositeDisposable.clear()
    }
}