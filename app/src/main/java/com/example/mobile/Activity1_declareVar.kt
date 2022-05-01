package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner

class Activity1_declareVar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1_declare_var)

        findViewById<ImageButton>(R.id.button3_1).setOnClickListener {
            startActivity(Intent(this, Activity1::class.java))
        }

        val spinner1 = findViewById<Spinner>(R.id.spinner1)
        ArrayAdapter.createFromResource(
            this,
            R.array.declaration,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner1.adapter = adapter
        }
    }
}