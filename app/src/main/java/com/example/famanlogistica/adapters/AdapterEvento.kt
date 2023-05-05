package com.example.famanlogistica.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.famanlogistica.R
import com.example.famanlogistica.classes.Clima
import com.example.famanlogistica.classes.Eventos
import java.text.SimpleDateFormat

class AdapterEvento(
    private val context: Context,
    private val arrayList: ArrayList<Eventos>,
    private val arrayClima: ArrayList<Clima>
    ) : RecyclerView.Adapter<AdapterEvento.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textData = itemView.findViewById<TextView>(R.id.text_date)
        val textTemperatura = itemView.findViewById<TextView>(R.id.text_temp)
        val listEvents = itemView.findViewById<RecyclerView>(R.id.list_eventos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertView = LayoutInflater.from(context).inflate(R.layout.adapter_items, parent, false)
        return ViewHolder(convertView)
    }

    override fun getItemCount(): Int {
        return if (arrayClima.size < arrayList.size) arrayClima.size
        else arrayList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val json = arrayClima[position]
        holder.textTemperatura.setText(json.condicao_desc)
        val dtTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val datas = ArrayList<Eventos>()
        arrayList.forEach { datas.add(it) }
        datas.sortedBy { it.data_solitaria }
            .distinctBy {
                it.data_solitaria
//                var formatedDate = dateFormat.parse(it.data)
//                dateFormat.parse(it.data)
            }

        if(datas.size > 0) {
            //val date = SimpleDateFormat("dd/MM/yyyy HH:mm").parse(datas[position].data)
            //val date = SimpleDateFormat("dd/MM/yyyy").parse(datas[position].data)
            val date = dateFormat.format(SimpleDateFormat("yyyy-MM-dd").parse(arrayClima[position].data))
            //holder.textData.setText(dateFormat.format(date) + " - ")
            holder.textData.setText("$date - ")
            val array = ArrayList<Eventos>()
            arrayList.forEach { array.add(it) }
            array.removeIf { item -> item.data_solitaria != dateFormat.format(SimpleDateFormat("yyyy-MM-dd").parse(arrayClima[position].data)) }
            array.sortBy {dtTimeFormat.parse(it.data)}
            holder.listEvents.layoutManager = LinearLayoutManager(context)
            holder.listEvents.adapter = AdapterItems(context, array)
        }
    }
}