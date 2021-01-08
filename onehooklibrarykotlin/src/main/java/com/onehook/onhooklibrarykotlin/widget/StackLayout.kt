package com.onehook.onhooklibrarykotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.core.view.marginBottom
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.core.view.updateMargins
import com.onehook.onhooklibrarykotlin.R
import com.onehook.onhooklibrarykotlin.view.WRAP_CONTENT
import kotlin.math.max
import kotlin.math.min

open class StackLayout : ViewGroup {

    enum class Orientation {
        VERTICAL,
        HORIZONTAL
    }

    enum class Gravity {
        LEFT_OR_TOP,
        CENTER,
        RIGHT_OR_BOTTOM
    }

    /**
     * Orientation. VERTICAL or HORIZONTAL.
     */
    var orientation = Orientation.HORIZONTAL

    /**
     * View Alignment.
     */
    private var gravity = Gravity.CENTER
    private var childGravity = Gravity.CENTER

    /**
     * Spacing.
     */
    var spacing: Float = 0f
        set(newValue) {
            field = newValue
            invalidate()
        }

    /* for horizontal */

    private var totalChildrenWidth: Float = 0f
    private var maxChildWidth: Float = 0f
    private var totalChildrenHeight: Float = 0f
    private var maxChildHeight: Float = 0f

    constructor(context: Context) : super(context) {
        commonInit(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        commonInit(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        commonInit(context, attrs)
    }

    /**
     * Common init with nullable attributes.
     *
     * @param context context
     * @param attrs   attributes
     */
    private fun commonInit(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.StackLayout)
            orientation = when (a.getInt(R.styleable.StackLayout_orientation, 0)) {
                1 -> Orientation.VERTICAL
                else -> Orientation.HORIZONTAL
            }
            gravity = when (a.getInt(R.styleable.StackLayout_gravity, 1)) {
                0 -> Gravity.LEFT_OR_TOP
                2 -> Gravity.RIGHT_OR_BOTTOM
                else -> Gravity.CENTER
            }
            childGravity = when (a.getInt(R.styleable.StackLayout_childGravity, 1)) {
                0 -> Gravity.LEFT_OR_TOP
                2 -> Gravity.RIGHT_OR_BOTTOM
                else -> Gravity.CENTER
            }
            spacing = a.getDimension(R.styleable.StackLayout_spacing, spacing)
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d("XXX", "on Measure called ${widthMeasureSpec} ${heightMeasureSpec}")
        totalChildrenWidth = 0f
        totalChildrenHeight = 0f
        maxChildWidth = 0f
        maxChildHeight = 0f
        var visibleChildCount = 0
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            Log.d("XXX", child.layoutParams.javaClass.name + ", " + child.marginStart)
            if (child.visibility == View.GONE) {
                continue
            }
            visibleChildCount += 1
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            totalChildrenWidth += child.measuredWidth
            maxChildWidth = max(maxChildWidth, child.measuredWidth.toFloat())
            totalChildrenHeight += child.measuredHeight
            maxChildHeight = max(maxChildHeight, child.measuredHeight.toFloat())
        }

        val widthWanted: Float
        val heightWanted: Float

        if (orientation == Orientation.HORIZONTAL) {
            totalChildrenWidth += spacing * max(0, visibleChildCount - 1)
            widthWanted = totalChildrenWidth + paddingStart + paddingEnd
            heightWanted = maxChildHeight + paddingTop + paddingBottom
        } else {
            totalChildrenHeight += spacing * max(0, visibleChildCount - 1)
            widthWanted = maxChildWidth + paddingStart + paddingEnd
            heightWanted = totalChildrenHeight + paddingTop + paddingBottom
        }

        setMeasuredDimension(
            resolveMeasureSpec(widthWanted.toInt(), widthMeasureSpec),
            resolveMeasureSpec(heightWanted.toInt(), heightMeasureSpec)
        )
    }


    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        Log.d("XXX", "generateLayoutParams called")
        return super.generateLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        Log.d("XXX", "generateLayoutParams attr called ${attrs}")
        val lp = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        return MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {

            val a = context.obtainStyledAttributes(attrs, R.styleable.ViewInfo)
            val margin =
                a.getDimension(R.styleable.ViewInfo_android_layout_margin, 0f)
            margin.toInt().also {
                updateMargins(left = it, top = it, right = it, bottom = it)
            }
            val marginStart = a.getDimension(R.styleable.ViewInfo_android_layout_margin, margin)
            val marginEnd = a.getDimension(R.styleable.ViewInfo_android_layout_margin, margin)
            val marginTop = a.getDimension(R.styleable.ViewInfo_android_layout_margin, margin)
            val marginBottom = a.getDimension(R.styleable.ViewInfo_android_layout_margin, margin)
            val gravity = a.getInt(R.styleable.ViewInfo_android_layout_gravity, 0)

            Log.d(
                "XXX",
                "try margin ${margin}, margin start ${marginStart}, gravity ${gravity}"
            )
//            val xmlProvidedSize =
//                attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_marginStart")

            Log.d("XXX", "${attrs?.attributeCount}")
            val m = attrs?.getAttributeIntValue(android.R.attr.layout_margin, -1) ?: 0
            val ms = attrs?.getAttributeIntValue(android.R.attr.layout_marginStart, -1) ?: 0
            Log.d("XXX", "${m}, ${ms}")
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        Log.d("XXX", "generateDefaultLayoutParams called")
        return MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    private fun resolveMeasureSpec(wanted: Int, measureSpec: Int): Int {
        val measure = MeasureSpec.getSize(measureSpec)
        return when (MeasureSpec.getMode(measureSpec)) {
            MeasureSpec.AT_MOST -> {
                min(measure, wanted)
            }
            else -> {
                measure
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val width = right - left
        val height = bottom - top
        if (orientation == Orientation.HORIZONTAL) {
            layoutHorizontally(width, height)
        } else {
            layoutVertically(width, height)
        }
    }

    /**
     * Layout Horizontally.
     *
     * @param width  view actual width (with padding included)
     * @param height view actual height (with padding included)
     */
    private fun layoutHorizontally(width: Int, height: Int) {
        val childCount = childCount
        var currX = when (gravity) {
            Gravity.CENTER -> (width - totalChildrenWidth.toInt()) / 2
            Gravity.LEFT_OR_TOP -> paddingStart
            Gravity.RIGHT_OR_BOTTOM -> width - paddingEnd - totalChildrenWidth.toInt()
        }
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            if (child.visibility == View.GONE) {
                continue
            }
            val currY = when (childGravity) {
                Gravity.CENTER -> (height - child.measuredHeight) / 2
                Gravity.LEFT_OR_TOP -> paddingTop
                Gravity.RIGHT_OR_BOTTOM -> measuredHeight - paddingBottom - child.measuredHeight
            }
            child.layout(currX, currY, currX + child.measuredWidth, currY + child.measuredHeight)
            currX += spacing.toInt() + child.measuredWidth
        }
    }

    /**
     * Layout Horizontally.
     *
     * @param width  view actual width (with padding included)
     * @param height view actual height (with padding included)
     */
    private fun layoutVertically(width: Int, height: Int) {
        val childCount = getChildCount()
        var currY = when (gravity) {
            Gravity.CENTER -> (height - totalChildrenHeight.toInt()) / 2
            Gravity.LEFT_OR_TOP -> paddingTop
            Gravity.RIGHT_OR_BOTTOM -> height - paddingBottom - totalChildrenHeight.toInt()
        }
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            if (child.visibility == View.GONE) {
                continue
            }
            val currX = when (childGravity) {
                Gravity.CENTER -> (width - child.measuredWidth) / 2
                Gravity.LEFT_OR_TOP -> paddingStart
                Gravity.RIGHT_OR_BOTTOM -> measuredWidth - paddingEnd - child.measuredWidth
            }
            child.layout(currX, currY, currX + child.measuredWidth, currY + child.measuredHeight)
            currY += spacing.toInt() + child.measuredHeight
        }
    }
}
