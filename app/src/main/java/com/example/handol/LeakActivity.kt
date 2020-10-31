package com.example.handol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LeakActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leak)
    }
}