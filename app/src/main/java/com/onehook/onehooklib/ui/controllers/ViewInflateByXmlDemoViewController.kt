package com.onehook.onehooklib.ui.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onehook.onehooklib.R
import com.onehook.onehooklib.ui.reusable.BaseDetailViewController

class ViewInflateByXmlDemoViewController : BaseDetailViewController() {
    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_view_inflate_by_xml, container, false).apply {
            setPadding(0, activity.safeArea.top, 0, 0)
        }
    }

    override fun viewDidLoad(view: View) {
        super.viewDidLoad(view)
        toolbar.title.getOrBuild().text = "Create View By XML"
    }
}