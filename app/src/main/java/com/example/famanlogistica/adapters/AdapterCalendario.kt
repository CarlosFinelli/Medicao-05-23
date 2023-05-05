package com.example.famanlogistica.adapters

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.famanlogistica.R
import com.example.famanlogistica.classes.Eventos

class AdapterCalendario(
    private val context: Context,
    private val arrayList: ArrayList<Eventos>,
    private val arrayDatas: ArrayList<String>
) : RecyclerView.Adapter<AdapterCalendario.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textData = itemView.findViewById<TextView>(R.id.text_data)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.recycler_events)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_calendario, parent, false))
    }

    override fun getItemCount(): Int {
        return arrayDatas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val data = arrayDatas[position]
        holder.textData.setText(SimpleDateFormat("dd MMMM yyyy").format(dateFormat.parse(data)))
        val array = ArrayList<Eventos>()
        if (arrayDatas.size > 0) {
            arrayList.forEach {
                if(it.data_solitaria == data) array.add(it)
            }
            holder.recyclerView.layoutManager = LinearLayoutManager(context)
            holder.recyclerView.adapter = AdapterItemsCalendario(context, array)
        }
    }
}