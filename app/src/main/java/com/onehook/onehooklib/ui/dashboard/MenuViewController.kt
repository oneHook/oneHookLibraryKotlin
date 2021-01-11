package com.onehook.onehooklib.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.onehook.onehooklib.ui.controllers.*
import com.onehook.onehooklib.ui.reusable.LargeTextView
import com.onehook.onehooklib.ui.reusable.MediumTextView
import com.onehook.onhooklibrarykotlin.graphics.DrawableFactory
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.utils.dpf
import com.onehook.onhooklibrarykotlin.view.LP
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.LinearRecyclerViewController
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.SimpleAdapter
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.ViewController
import com.onehook.onhooklibrarykotlin.widget.Toolbar

private interface Item
private data class Section(val title: String) : Item
private data class MenuItem(
    val title: String,
    val subtitle: String?,
    val action: () -> ViewController
) : Item

private class SectionCell(context: Context) : LargeTextView(context) {
    init {
        layoutParams = LP().apply {
            width = MATCH_PARENT
        }
        letterSpacing = 0.1f
        setTextColor(Color.WHITE)
        setPadding(dp(10), dp(15), dp(10), dp(15))
        setBackgroundColor(Color.DKGRAY)
    }
}

private class ItemCell(context: Context) : FrameLayout(context) {

    val titleLabel: MediumTextView by lazy {
        MediumTextView(context).apply {
            letterSpacing = 0.1f
            setPadding(dp(10), dp(15), dp(10), dp(15))
        }
    }

    init {
        layoutParams = LP().apply {
            width = MATCH_PARENT
        }
        addView(titleLabel)
        val drawable = DrawableFactory.backgroundRippleDrawable(Color.WHITE, Color.GRAY)
        background = drawable
    }
}

class MenuViewController : LinearRecyclerViewController() {

    private val items = ArrayList<Item>()

    init {
        items.add(Section(title = "View Inflation"))
        items.add(
            MenuItem(
                title = "By Code",
                subtitle = null,
                action = { ViewInflateByCodeDemoViewController() })
        )
        items.add(
            MenuItem(
                title = "By Xml",
                subtitle = null,
                action = { ViewInflateByXmlDemoViewController() })
        )
        items.add(
            MenuItem(
                title = "By Code Without Auto Layout",
                subtitle = null,
                action = { ViewInflateByManualCodeDemoViewController() })
        )

        items.add(Section(title = "Views"))
        items.add(
            MenuItem(
                title = "StackLayout",
                subtitle = null,
                action = { StackLayoutDemoViewController() })
        )
        items.add(
            MenuItem(
                title = "Line Graph View",
                subtitle = null,
                action = { LineGraphDemoViewController() })
        )

        items.add(Section(title = "Navigation"))
        for (i in 0..20) {
            items.add(
                MenuItem(
                    title = "Navigation Demo",
                    subtitle = null,
                    action = { ControllerNavigationDemoViewController() })
            )
        }
    }

    private val toolbar: Toolbar by lazy {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return super.onCreateView(inflater, container).also {
            (it as? ViewGroup)?.addView(toolbar)
        }
    }

    override fun viewDidLoad(view: View) {
        super.viewDidLoad(view)
        view.setBackgroundColor(Color.WHITE)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                when (parent.getChildAdapterPosition(view)) {
                    0 -> {
                        outRect.set(0, activity.safeArea.top + dp(56), 0, 0)
                    }
                    items.size - 1 -> {
                        outRect.set(0, 0, 0, activity.safeArea.bottom)
                    }
                }
            }
        })
        recyclerView.adapter = object : SimpleAdapter() {

            override fun onCreateView(parent: ViewGroup, viewType: Int): View {
                return when (viewType) {
                    0 -> SectionCell(parent.context)
                    else -> ItemCell(parent.context)
                }
            }

            override fun getItemViewType(position: Int): Int {
                return when {
                    items[position] is Section -> 0
                    else -> 1
                }
            }

            override fun onBindView(view: View, position: Int) {
                val viewType = getItemViewType(position)
                val item = items.get(position)
                when (viewType) {
                    0 -> {
                        (view as? SectionCell)?.text = (item as? Section)?.title ?: ""
                    }
                    else -> {
                        val menuItem = item as? MenuItem
                        (view as? ItemCell)?.titleLabel?.text = menuItem?.title ?: ""
                        view.setOnClickListener {
                            menuItem?.action?.invoke()?.also {
                                navigationController?.push(it, true)
                            }
                        }
                    }
                }
            }

            override fun getItemCount(): Int {
                return items.size
            }
        }
    }
}