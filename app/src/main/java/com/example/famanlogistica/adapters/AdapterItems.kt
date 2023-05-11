package com.example.famanlogistica.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.famanlogistica.R
import com.example.famanlogistica.classes.Eventos
import java.text.SimpleDateFormat
import java.util.Date

class AdapterItems(
    private val context: Context,
    private val arrayList: ArrayList<Eventos>
) : RecyclerView.Adapter<AdapterItems.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textHorario = itemView.findViewById<TextView>(R.id.text_horario)
        val textDesc = itemView.findViewById<TextView>(R.id.text_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_dates, parent, false))
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = arrayList[position]
        var data = item.data
        data = data.substringAfter(' ')
        holder.textHorario.setText(data)
        holder.textDesc.setText(item.titulo)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}