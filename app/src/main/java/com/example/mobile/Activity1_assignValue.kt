package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner

class Activity1_assignValue : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1_assign_val)

        findViewById<ImageButton>(R.id.button3_2).setOnClickListener {
            startActivity(Intent(this, Activity1::class.java))
        }

        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val assignment = arrayOf<String?>()
        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this, R.layout.spinner_list, assignment)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)
        spinner2.adapter = arrayAdapter
    }
}