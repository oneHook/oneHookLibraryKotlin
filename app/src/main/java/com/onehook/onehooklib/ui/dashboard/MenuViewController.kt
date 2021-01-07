package com.onehook.onehooklib.ui.dashboard

import android.graphics.Color
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.onehook.onhooklibrarykotlin.utils.dp
import com.onehook.onhooklibrarykotlin.view.MATCH_PARENT
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.LinearRecyclerViewController
import com.onehook.onhooklibrarykotlin.viewcontroller.controller.SimpleAdapter

class MenuViewController : LinearRecyclerViewController() {

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
                    5 -> {
                        outRect.set(0, 0, 0, activity.safeArea.bottom)
                    }
                }
            }
        })
        recyclerView.adapter = object : SimpleAdapter() {
            override fun onCreateView(parent: ViewGroup, viewType: Int): View {
                return AppCompatTextView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, dp(100) * (viewType + 1))
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    text = viewType.toString()
                    textSize = dp(25).toFloat()

                    if (viewType % 2 == 0) {
                        setBackgroundColor(Color.RED)
                    } else {
                        setBackgroundColor(Color.GREEN)
                    }
                }
            }

            override fun getItemViewType(position: Int): Int = position
            override fun onBindView(view: View, position: Int) {
            }

            override fun getItemCount(): Int {
                return 6
            }
        }
    }

    override fun viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
    }
}