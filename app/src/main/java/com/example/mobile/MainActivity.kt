package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button1).setOnClickListener {
            startActivity(Intent(this, Activity1::class.java))
        }

        findViewById<Button>(R.id.button1_exit).setOnClickListener {
            finishAffinity()
        }
    }
}