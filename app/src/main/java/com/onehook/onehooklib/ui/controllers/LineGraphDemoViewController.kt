package com.onehook.onehooklib.ui.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.onehook.onehooklib.ui.reusable.BaseDetailViewController
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.view.LP
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import com.onehook.onhooklibrarykotlin.widget.LineGraphUIModel
import com.onehook.onhooklibrarykotlin.widget.LineGraphView

class LineGraphDemoViewController : BaseDetailViewController() {

    lateinit var graph1: LineGraphView

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View {
        return LinearLayoutCompat(container.context).apply {
            orientation = LinearLayoutCompat.VERTICAL
            setPadding(0, dp(56) + activity.safeArea.top, 0, 0)

            addView(LineGraphView(container.context).apply {
                layoutParams = LP().apply {
                    width = MATCH_PARENT
                    height = dp(250)
                }.linearLayoutLp()
                graph1 = this
            })
        }
    }

    override fun viewDidLoad(view: View) {
        super.viewDidLoad(view)

        graph1.bind(LineGraphUIModel(intArrayOf(1, 5, 4, 2, 4, 5, 2, 3, 1)), animated = false)
    }
}