package com.example.famanlogistica.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.famanlogistica.R
import com.example.famanlogistica.classes.Eventos
import com.example.famanlogistica.helpers.DatabaseHelper
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar

class AdapterItemsCalendario(
    private val context: Context,
    private val arrayList: ArrayList<Eventos>
) : RecyclerView.Adapter<AdapterItemsCalendario.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textHorario = itemView.findViewById<TextView>(R.id.text_horario)
        val textDesc = itemView.findViewById<TextView>(R.id.text_desc)
        val btnDelete = itemView.findViewById<ImageFilterView>(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_items_calendario, parent, false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeFormat = SimpleDateFormat("HH:mm")
        val inicio = timeFormat.parse(arrayList[position].data.substringAfter(' '))
        val tolerancia = timeFormat.parse(arrayList[position].tolerancia)
        val fim = Calendar.getInstance()
        fim.set(Calendar.HOUR_OF_DAY, inicio.hours + tolerancia.hours)
        fim.set(Calendar.MINUTE, inicio.minutes + tolerancia.minutes)
        holder.textDesc.setText(arrayList[position].descricao)
        holder.textHorario.setText("${timeFormat.format(fim.time)} - ")

        if (holder.btnDelete != null) {
            holder.btnDelete.setOnClickListener {
                val db = DatabaseHelper(context)
                db.deleteEvento(arrayList[position].id)
                arrayList.remove(arrayList[position])
                notifyItemRemoved(position)
            }
        }
    }
}