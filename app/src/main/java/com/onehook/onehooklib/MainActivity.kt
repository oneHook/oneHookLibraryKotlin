package com.onehook.onehooklib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onehook.onhooklibrarykotlin.foo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val n = foo(1, 2)
    }
}
