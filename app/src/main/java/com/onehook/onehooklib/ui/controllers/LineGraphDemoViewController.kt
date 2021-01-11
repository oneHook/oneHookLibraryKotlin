package com.onehook.onehooklib.ui.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.onehook.onehooklib.ui.reusable.BaseDetailViewController
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.view.LP
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import com.onehook.onhooklibrarykotlin.widget.LineGraphView

class LineGraphDemoViewController : BaseDetailViewController() {

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View {
        return LinearLayoutCompat(container.context).apply {
            orientation = LinearLayoutCompat.VERTICAL
            setPadding(0, dp(56) + activity.safeArea.top, 0, 0)

            addView(LineGraphView(container.context).apply {
                layoutParams = LP().apply {
                    width = MATCH_PARENT
                    height = dp(250)
                }.linearLayoutLp()
            })
        }
    }
}