package com.onehook.onhooklibrarykotlin.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.onehook.onhooklibrarykotlin.utils.dpf


data class LineGraphUIModel(
    val points: ArrayList<Float>,
    val smooth: Boolean = true
) {
    constructor(
        numbers: IntArray,
        minValue: Int? = null,
        maxValue: Int? = null,
        smooth: Boolean = true
    ) : this(points = arrayListOf<Float>(), smooth = smooth) {
        val minV = (minValue ?: numbers.minOrNull() ?: 0).toFloat()
        val maxV = (maxValue ?: numbers.maxOrNull() ?: 0).toFloat()
        numbers.forEach {
            points.add(1 - (it.toFloat() - minV) / (maxV - minV))
        }
    }
}

open class LineGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyle, defStyleRes) {

    private var needsRefresh = false
    private val paint = Paint().apply {
        strokeWidth = dpf(2)
        color = Color.WHITE
        style = Paint.Style.STROKE
    }
    private val gradientPaint = Paint().apply {

    }
    private val path = Path()
    private var uiModel = LineGraphUIModel(points = arrayListOf(), smooth = false)

    init {
        setWillNotDraw(false)
        setBackgroundColor(Color.RED)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas == null) {
            return
        }
        if (isInEditMode) {
            /* For Android studio preview */
            bind(
                uiModel = LineGraphUIModel(intArrayOf(2, 5, 1, 4, 2, 3, 1), -2, 6),
                animated = false
            )
        }


        canvas.clipPath(path)

        gradientPaint.setShader(
            LinearGradient(
                0f, 0f, 0f,
                canvas.height.toFloat(), Color.WHITE, Color.BLACK, Shader.TileMode.MIRROR
            )
        )
        canvas.drawPaint(gradientPaint)

        paint.style = Paint.Style.STROKE
        canvas.drawPath(path, paint)


    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)
        refresh(animated = false)
    }

    private fun refresh(animated: Boolean) {
        val width = measuredWidth - paddingLeft - paddingRight
        val height = measuredHeight - paddingTop - paddingBottom
        if (!needsRefresh || uiModel.points.isEmpty() || width <= 0 || height <= 0) {
            return
        }

        val segmentCount = uiModel.points.size
        val segmentWidth = width / (segmentCount - 1)
        val points = mutableListOf<PointF>()

        uiModel.points.forEachIndexed { index, num ->
            points.add(
                PointF(
                    (index * segmentWidth + paddingStart).toFloat(),
                    height * num + paddingTop
                )
            )
        }

        path.reset()
        points.forEach {
            if (path.isEmpty) {
                path.moveTo(it.x, it.y)
            } else {
                path.lineTo(it.x, it.y)
            }
        }
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(paddingLeft.toFloat(), height.toFloat())
        path.close()

        invalidate()
        needsRefresh = false
    }


    fun bind(uiModel: LineGraphUIModel, animated: Boolean) {
        needsRefresh = true
        this.uiModel = uiModel
        refresh(animated = animated)
    }
}