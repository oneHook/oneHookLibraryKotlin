package com.onehook.onhooklibrarykotlin.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.onehook.onhooklibrarykotlin.utils.dpf

open class LineGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyle, defStyleRes) {

    private val paint = Paint().apply {
        strokeWidth = dpf(2)
        color = Color.WHITE
        style = Paint.Style.STROKE
    }
    private val path = Path()

    init {
        setWillNotDraw(false)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas == null) {
            return
        }
        canvas.drawColor(Color.RED)
        canvas.drawPath(path, paint)
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)
        val width = r - l
        val height = b - t
        path.reset()
        path.moveTo(0f, 0f)
        path.moveTo(dpf(150), dpf(150))
    }
}