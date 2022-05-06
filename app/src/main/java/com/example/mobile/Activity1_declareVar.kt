package com.example.mobile

import android.app.Activity
import android.app.Person
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class Activity1_declareVar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1_declare_var)

        findViewById<ImageButton>(R.id.button3_1).setOnClickListener {
            startActivity(Intent(this, Activity1::class.java))
        }

        val spinner1 = findViewById<Spinner>(R.id.spinner1)
        val declaration = arrayOf<String?>("Bool", "Int", "Double", "String")
        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this, R.layout.spinner_list, declaration)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)
        spinner1.adapter = arrayAdapter

//        val spinner1 = findViewById<Spinner>(R.id.spinner1)
//        spinner1.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val text: String = parent?.getItemAtPosition(position).toString() }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented") }
//        }

        val launchButton = findViewById<Button>(R.id.button1_1)
        launchButton.setOnClickListener {
            callActivity()
        }

        val editText = findViewById<EditText>(R.id.edittext1)

        editText.setOnClickListener {
            it.hideKeyboard()
        }
    }

    private fun callActivity() {
        val editText = findViewById<EditText>(R.id.edittext1)
        val message = editText.text.toString()
        val intent = Intent(this, Activity1::class.java).also {
            it.putExtra("EXTRA_MESSAGE", message)
            startActivity(it)
        }
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}