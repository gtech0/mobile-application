package com.example.mobile

import android.appwidget.AppWidgetHostView
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*

class Activity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        findViewById<ImageButton>(R.id.button3).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val button4 = findViewById<ImageButton>(R.id.button4)
        button4.setOnClickListener {
            showPopup(button4)
        }

        val message = intent.getStringExtra("EXTRA_MESSAGE")
        val textView = findViewById<TextView>(R.id.textview6).apply {
            text = message
        }
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.popup_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when(item!!.itemId) {
                R.id.declare_variable -> {
                    //Toast.makeText(this@Activity1, item.title, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,Activity1_declareVar::class.java))
                }

                R.id.assign_value -> {
                    //Toast.makeText(this@Activity1, item.title, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,Activity1_assignValue::class.java))
                }
            }
            true
        })

        popup.show()
    }
}