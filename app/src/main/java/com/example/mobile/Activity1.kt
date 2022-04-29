package com.example.mobile

import android.appwidget.AppWidgetHostView
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast

class Activity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        val button3 = findViewById<ImageButton>(R.id.button3)
        button3.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val button4 = findViewById<ImageButton>(R.id.button4)
        button4.setOnClickListener {
            showPopup(button4)
        }
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.popup_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when(item!!.itemId) {
                R.id.declare_variable -> {
                    Toast.makeText(this@Activity1, item.title, Toast.LENGTH_SHORT).show()
                }

                R.id.assign_value -> {
                    Toast.makeText(this@Activity1, item.title, Toast.LENGTH_SHORT).show()
                }
            }
            true
        })

        popup.show()
    }
}