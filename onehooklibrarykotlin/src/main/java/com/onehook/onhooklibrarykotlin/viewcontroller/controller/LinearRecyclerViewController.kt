package com.onehook.onhooklibrarykotlin.viewcontroller.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DefaultViewHolder(val view: View) : RecyclerView.ViewHolder(view)

abstract class SimpleAdapter : RecyclerView.Adapter<DefaultViewHolder>() {
    protected abstract fun onCreateView(parent: ViewGroup, viewType: Int): View
    protected abstract fun onBindView(view: View, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultViewHolder {
        return DefaultViewHolder(onCreateView(parent, viewType))
    }

    override fun onBindViewHolder(holder: DefaultViewHolder, position: Int) {
        onBindView(holder.view, position)
    }
}

open class LinearRecyclerViewController : ViewController() {

    val recyclerView: RecyclerView by lazy {
        RecyclerView(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val frameLayout = FrameLayout(container.context)
        frameLayout.addView(recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
        })
        return frameLayout
    }
}