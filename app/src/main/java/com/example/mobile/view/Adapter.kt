package com.example.mobile.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.models.DeclareData

class Adapter(val c: Context, val declareList: ArrayList<DeclareData>) :
    RecyclerView.Adapter<Adapter.DeclareViewHolder>() {
    inner class DeclareViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val type_a = view.findViewById<TextView>(R.id.type_assign)
        val name_a = view.findViewById<TextView>(R.id.name_assign)
    }

    fun deleteBlock(iter : Int){
        declareList.removeAt(iter)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeclareViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.block_dialog, parent, false)
        return DeclareViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeclareViewHolder, position: Int) {
        val newList = declareList[position]
        holder.name_a.text = newList.name
        holder.type_a.text = newList.type
    }

    override fun getItemCount(): Int {
        return declareList.size
    }
}