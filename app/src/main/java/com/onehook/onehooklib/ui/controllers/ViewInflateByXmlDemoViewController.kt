package com.onehook.onehooklib.ui.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onehook.onehooklib.R
import com.onehook.onehooklib.ui.reusable.BaseViewController

class ViewInflateByXmlDemoViewController : BaseViewController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_view_inflate_by_xml, container, false)
    }

    override fun viewDidLoad(view: View) {
        super.viewDidLoad(view)
    }
}