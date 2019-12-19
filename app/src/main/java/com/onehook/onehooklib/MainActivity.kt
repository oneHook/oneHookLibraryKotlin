package com.onehook.onehooklib

import android.graphics.Color
import android.os.Bundle
import com.onehook.onhooklibrarykotlin.app.OHActivity

class MainActivity : OHActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view.setBackgroundColor(Color.RED)
    }
}
