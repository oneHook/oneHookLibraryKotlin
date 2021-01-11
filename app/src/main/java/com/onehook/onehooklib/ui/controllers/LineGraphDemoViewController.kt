package com.onehook.onehooklib.ui.controllers

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.onehook.onehooklib.ui.reusable.BaseDetailViewController
import com.onehook.onehooklib.ui.reusable.RoundedSolidButton
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.view.LP
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import com.onehook.onhooklibrarykotlin.widget.LineGraphUIModel
import com.onehook.onhooklibrarykotlin.widget.LineGraphView
import kotlin.random.Random

class LineGraphDemoViewController : BaseDetailViewController() {

    lateinit var graph1: LineGraphView
    lateinit var graph2: LineGraphView

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View {
        return LinearLayout(container.context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(56) + activity.safeArea.top, 0, 0)

            addView(LineGraphView(container.context).apply {
                layoutParams = LP().apply {
                    width = MATCH_PARENT
                    height = dp(250)
                    margin = dp(10)
                }.linearLayoutLp()
                setBackgroundColor(Color.WHITE)
                setGradientColor(Color.RED, Color.BLACK)
                graph1 = this
            })
            addView(LineGraphView(container.context).apply {
                layoutParams = LP().apply {
                    width = MATCH_PARENT
                    height = dp(250)
                    margin = dp(10)
                }.linearLayoutLp()
                setBackgroundColor(Color.WHITE)
                setGradientColor(Color.RED, Color.WHITE)
                setStrokeColor(Color.YELLOW)
                graph2 = this
            })

            addView(RoundedSolidButton(container.context, Color.BLUE).apply {
                layoutParams = LP().apply {
                    width = dp(200)
                    layoutGravity = Gravity.CENTER_HORIZONTAL
                    topMargin = dp(55)
                }.linearLayoutLp()
                text = "Random"
                setOnClickListener {
                    randomGraph(animated = true)
                }
            })
        }
    }

    override fun viewDidLoad(view: View) {
        super.viewDidLoad(view)
        toolbar.title.getOrBuild().text = "Line Graph Demo"
        randomGraph(animated = false)
    }

    private fun randomGraph(animated: Boolean) {
        val graphs = arrayOf(graph1, graph2)

        val intArray = IntArray(20)
        for (i in 0 until 20) {
            intArray[i] = Random.nextInt(0, 100)
        }
        graphs.forEachIndexed { index, graph ->
            graph.bind(LineGraphUIModel(numbers = intArray, minValue = -10, maxValue = 110, smooth = index % 2 == 0), animated = animated)
        }
    }
}