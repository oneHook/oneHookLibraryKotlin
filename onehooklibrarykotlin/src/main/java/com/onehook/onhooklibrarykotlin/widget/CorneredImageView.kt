package com.onehook.onhooklibrarykotlin.widget;

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Size
import androidx.appcompat.widget.AppCompatImageView
import com.onehook.onhooklibrarykotlin.R
import com.onehook.onhooklibrarykotlin.utils.dpf

class CorneredImageView : AppCompatImageView {

    private val fillPaint = Paint()
    private val pathSize = Size(0, 0)
    private var path: Path? = null
    var fillColor: Int = Color.parseColor("#FFFFFF")
        set(value) {
            field = value
            fillPaint.color = value
            invalidate()
        }
    var topLeftCornerRadius: Float = dpf(5)
    var topRightCornerRadius: Float = dpf(5)
    var bottomRightCornerRadius: Float = dpf(5)
    var bottomLeftCornerRadius: Float = dpf(5)

    constructor(context: Context) : super(context) {
        commonInit(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        commonInit(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        commonInit(context, attrs)
    }

    private fun commonInit(context: Context, attrs: AttributeSet?) {
        setWillNotDraw(false)
        fillPaint.color = fillColor
        fillPaint.style = Paint.Style.FILL
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CorneredImageView)
            topLeftCornerRadius = typedArray.getDimension(
                R.styleable.CorneredImageView_corner_radius,
                topLeftCornerRadius
            )
            topRightCornerRadius = topLeftCornerRadius
            bottomLeftCornerRadius = topLeftCornerRadius
            bottomRightCornerRadius = topLeftCornerRadius
            typedArray.recycle()
        }
    }

    private fun invalidatePathIfNeeded(width: Int, height: Int) {
        if (path == null || pathSize.width != width || pathSize.height != height) {
            val outerPath = Path()
            outerPath.addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
            val innerPath = Path()
            innerPath.addRoundRect(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                floatArrayOf(
                    topLeftCornerRadius, topLeftCornerRadius,
                    topRightCornerRadius, topRightCornerRadius,
                    bottomRightCornerRadius, bottomRightCornerRadius,
                    bottomLeftCornerRadius, bottomLeftCornerRadius
                ), Path.Direction.CW
            )
            outerPath.op(innerPath, Path.Op.DIFFERENCE)
            this.path = outerPath
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.let { canvas ->
            invalidatePathIfNeeded(measuredWidth, measuredHeight)
            path?.let {
                canvas.drawPath(it, fillPaint)
            }
        }

    }
}