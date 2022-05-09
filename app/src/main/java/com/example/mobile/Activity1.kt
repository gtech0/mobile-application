package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.models.DeclareData
import com.example.mobile.view.Adapter
import com.example.mobile.view.SwipeGes
import java.util.*
import kotlin.collections.ArrayList

class Activity1 : AppCompatActivity() {
//    var x : Double? = 0.0
//    var y : Double? = 0.0
    private lateinit var recycle: RecyclerView
    private lateinit var listD: ArrayList<DeclareData>
    private lateinit var adapter1: Adapter
    //@SuppressLint("ClickableViewAccessibility")
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

        recycle = findViewById(R.id.recycler)
        listD = ArrayList()
        adapter1 = Adapter(this,listD)
        recycle.layoutManager = LinearLayoutManager(this)
        recycle.adapter = adapter1

        val swipeGes = object : SwipeGes() {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                Collections.swap(listD, from, to)
                adapter1.notifyItemMoved(from, to)
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction) {
                    ItemTouchHelper.RIGHT -> {
                        adapter1.deleteBlock(viewHolder.adapterPosition)
                    }

                    ItemTouchHelper.LEFT -> {
                        adapter1.deleteBlock(viewHolder.adapterPosition)
                    }
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipeGes)
        touchHelper.attachToRecyclerView(recycle)

//        textView.setOnTouchListener{view , event->
//            when (event.action){
//                MotionEvent.ACTION_DOWN -> {
//                    x = view.x.toDouble() - event.rawX
//                    y = view.y.toDouble() - event.rawY
//                    true
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    textView.animate()
//                        .x(event.rawX + x!!.toFloat())
//                        .y(event.rawY + y!!.toFloat())
//                        .setDuration(0)
//                        .start()
//                    true
//                }
//                else -> {
//                    true
//                }
//            }
//        }
    }

    private fun addInfoD() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.list_declare, null)
        val type = v.findViewById<EditText>(R.id.edittext2)
        val name = v.findViewById<EditText>(R.id.edittext1)
        val addDialog = AlertDialog.Builder(this)
        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            val names = name.text.toString()
            val types = type.text.toString()
            listD.add(DeclareData("Type: $types", "Name: $names"))
            adapter1.notifyDataSetChanged()
            Toast.makeText(this, "Added",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Nope"){
                dialog,_->
            Toast.makeText(this,"Hell no",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.create()
        addDialog.show()
    }

    private fun addInfoA() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.list_assign, null)
        val type = v.findViewById<EditText>(R.id.edittext4)
        val name = v.findViewById<EditText>(R.id.edittext3)
        val addDialog = AlertDialog.Builder(this)
        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            val names = name.text.toString()
            val types = type.text.toString()
            listD.add(DeclareData("Name: $types", "Value: $names"))
            adapter1.notifyDataSetChanged()
            Toast.makeText(this, "Assigned",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Nope"){
                dialog,_->
            Toast.makeText(this,"Hell no",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.create()
        addDialog.show()
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.popup_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when(item!!.itemId) {
                R.id.declare_variable -> {
                    addInfoD()
                }

                R.id.assign_value -> {
                    addInfoA()
                }
            }
            true
        })
        popup.show()
    }
}