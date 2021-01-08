package com.onehook.onehooklib.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.onehook.onehooklib.ui.controllers.ViewInflateByCodeDemoViewController
import com.onehook.onehooklib.ui.controllers.ViewInflateByXmlDemoViewController
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.view.LP
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.LinearRecyclerViewController
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.SimpleAdapter
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.ViewController

private interface Item
private data class Section(val title: String) : Item
private data class MenuItem(
    val title: String,
    val subtitle: String?,
    val action: () -> ViewController
) : Item

private class SectionCell(context: Context) : AppCompatTextView(context) {
    init {
        layoutParams = LP().apply {
            width = MATCH_PARENT
        }
        letterSpacing = 0.1f
        textSize = 14f
        setTextColor(Color.WHITE)
        setPadding(dp(10), dp(15), dp(10), dp(15))
        setBackgroundColor(Color.DKGRAY)
    }
}

private class ItemCell(context: Context) : AppCompatTextView(context) {
    init {
        layoutParams = LP().apply {
            width = MATCH_PARENT
        }
        letterSpacing = 0.1f
        textSize = 12f
        setPadding(dp(10), dp(15), dp(10), dp(15))
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
                        outRect.set(0, activity.safeArea.top, 0, 0)
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
                        (view as? ItemCell)?.text = menuItem?.title ?: ""
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

    override fun viewDidAppear(animated: Boolean) {
        super.viewDidAppear(animated)
    }

    override fun viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
    }
}