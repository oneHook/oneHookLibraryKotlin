package com.onehook.onhooklibrarykotlin.widget

import android.animation.Animator
import android.animation.FloatArrayEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.onehook.onhooklibrarykotlin.utils.dpf
import kotlin.math.abs


data class LineGraphUIModel(
    val points: FloatArray,
    val smooth: Boolean = true
) {
    constructor(
        numbers: IntArray,
        minValue: Int? = null,
        maxValue: Int? = null,
        smooth: Boolean = false
    ) : this(points = FloatArray(numbers.size), smooth = smooth) {
        val minV = (minValue ?: numbers.minOrNull() ?: 0).toFloat()
        val maxV = (maxValue ?: numbers.maxOrNull() ?: 0).toFloat()
        numbers.forEachIndexed { index, it ->
            points[index] = 1 - (it.toFloat() - minV) / (maxV - minV)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LineGraphUIModel

        if (!points.contentEquals(other.points)) return false
        if (smooth != other.smooth) return false

        return true
    }

    override fun hashCode(): Int {
        var result = points.contentHashCode()
        result = 31 * result + smooth.hashCode()
        return result
    }
}

open class LineGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyle, defStyleRes) {

    private var needsRefresh = false
    private val strokePaint = Paint().apply {
        strokeWidth = dpf(8)
        color = strokeColor
        style = Paint.Style.STROKE
    }
    private val gradientPaint = Paint()
    private val maskPath = Path()
    private val strokePath = Path()
    private val tempPoint = PointF()
    private var uiModel = LineGraphUIModel(points = FloatArray(0), smooth = false)

    @ColorInt
    private
    var startColor: Int = Color.WHITE

    @ColorInt
    private
    var endColor: Int = Color.BLACK

    @ColorInt
    private
    var strokeColor: Int = Color.GRAY

    init {
        setWillNotDraw(false)

        if (isInEditMode) {
            /* For Android studio preview */
            bind(
                uiModel = LineGraphUIModel(
                    intArrayOf(2, 5, 1, 4, 2, 3, 1), -2, 6,
                    smooth = true
                ),
                animated = false
            )
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas == null) {
            return
        }

        canvas.clipPath(maskPath)

        gradientPaint.shader = LinearGradient(
            0f, 0f, 0f,
            height.toFloat(), startColor, endColor, Shader.TileMode.MIRROR
        )
        canvas.drawPaint(gradientPaint)
        canvas.drawPath(strokePath, strokePaint)
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)
        refresh(points = uiModel.points, smooth = uiModel.smooth)
    }

    private fun refresh(points: FloatArray, smooth: Boolean) {
        val width = measuredWidth - paddingLeft - paddingRight
        val height = measuredHeight - paddingTop - paddingBottom
        if (!needsRefresh || points.isEmpty() || width <= 0 || height <= 0) {
            return
        }

        val segmentCount = points.size
        val segmentWidth = width / (segmentCount - 1)

        maskPath.rewind()
        fillPath(
            path = maskPath,
            segmentWidth = segmentWidth.toFloat(),
            height = height.toFloat(),
            ys = points,
            smooth = smooth
        )
        maskPath.lineTo(paddingStart + width.toFloat(), paddingTop + height.toFloat())
        maskPath.lineTo(paddingLeft.toFloat(), paddingTop + height.toFloat())
        maskPath.close()

        strokePath.rewind()
        fillPath(
            path = strokePath,
            segmentWidth = segmentWidth.toFloat(),
            height = height.toFloat(),
            ys = points,
            smooth = smooth
        )

        invalidate()
        needsRefresh = false
    }

    private fun fillPath(
        path: Path,
        segmentWidth: Float,
        height: Float,
        ys: FloatArray,
        smooth: Boolean
    ) {
        if (ys.size <= 1) {
            return
        }
        if (smooth) {
            fillQuadCurvedPath(path = path, segmentWidth = segmentWidth, height = height, ys = ys)
        } else {
            ys.forEachIndexed { index, yRatio ->
                val x = paddingLeft + index * segmentWidth
                val y = paddingTop + height * yRatio
                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
        }
    }

    private fun fillQuadCurvedPath(
        path: Path,
        segmentWidth: Float,
        height: Float,
        ys: FloatArray
    ) {
        var valueX = paddingLeft.toFloat()
        var valueY = paddingTop + height * ys[0]
        path.moveTo(valueX, valueY)
        if (ys.size == 2) {
            valueX = paddingLeft + segmentWidth
            valueY = paddingTop + height * ys[1]
            path.lineTo(valueX, valueY)
            return
        }
        for (i in 1 until ys.size) {
            val nextX = paddingLeft + i * segmentWidth
            val nextY = paddingTop + height * ys[i]
            val midX = segmentWidth * (i - 0.5f)
            val midY = (valueY + nextY) / 2

            controlPoint(midX, midY, valueX, valueY)
            path.quadTo(tempPoint.x, tempPoint.y, midX, midY)
            controlPoint(midX, midY, nextX, nextY)
            path.quadTo(tempPoint.x, tempPoint.y, nextX, nextY)

            valueX = nextX
            valueY = nextY
        }
    }

    private fun controlPoint(x1: Float, y1: Float, x2: Float, y2: Float) {
        tempPoint.x = (x1 + x2) / 2
        tempPoint.y = (y1 + y2) / 2
        val diffY = abs(y2 - tempPoint.y)
        if (y1 < y2) {
            tempPoint.y += diffY
        } else if (y1 > y2) {
            tempPoint.y -= diffY
        }
    }

    fun setGradientColor(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        this.startColor = startColor
        this.endColor = endColor
        invalidate()
    }

    fun setStrokeColor(@ColorInt strokeColor: Int) {
        this.strokeColor = strokeColor
        strokePaint.color = strokeColor
        invalidate()
    }

    fun bind(uiModel: LineGraphUIModel, animated: Boolean) {
        if (animated) {
            getAnimator(uiModel.points).setDuration(250).start()
        } else {
            needsRefresh = true
            refresh(points = uiModel.points, smooth = uiModel.smooth)
        }
        this.uiModel = uiModel
    }

    fun getAnimator(newPoints: FloatArray): Animator {
        return ValueAnimator.ofObject(
            FloatArrayEvaluator(),
            uiModel.points,
            newPoints
        ).apply {
            addUpdateListener {
                needsRefresh = true
                val points = it.animatedValue as FloatArray
                refresh(points = points, smooth = uiModel.smooth)
            }
        }
    }
}