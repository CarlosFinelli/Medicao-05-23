package com.example.famanlogistica.views.calendario

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.famanlogistica.adapters.AdapterCalendario
import com.example.famanlogistica.adapters.AdapterItems
import com.example.famanlogistica.adapters.AdapterItemsCalendario
import com.example.famanlogistica.classes.Eventos
import com.example.famanlogistica.databinding.ActivityCalendarioBinding
import com.example.famanlogistica.helpers.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar

class CalendarioViewModel : ViewModel() {

    fun setComponents(context: Context, binding: ActivityCalendarioBinding) {
        val mainLayout = binding.mainLayout
        val calendario = binding.calendario
        calendario.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val simpleDateFormat = SimpleDateFormat("dd MMMM ")
            val date = Calendar.getInstance()
            date.set(year, month, dayOfMonth)
            mainLayout.alpha = 0.5f
            binding.cardDialog.visibility = View.VISIBLE
            binding.textTitle.setText(simpleDateFormat.format(date.time))
            showDay(context, binding, date)
        }

        binding.btnClose.setOnClickListener {
            binding.cardDialog.visibility = View.GONE
            mainLayout.alpha = 1f
        }

        loadData(context, binding)
    }

    private fun loadData(context: Context, binding: ActivityCalendarioBinding) {
        val db = DatabaseHelper(context)
        val arrayData = db.selectAll(SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time))
        val arrayDatas = ArrayList<String>()
        arrayDatas.add(SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time))
        arrayData.sortBy { it.data_solitaria }
//        arrayData.forEach {
//            arrayDatas.add(it.data_solitaria)
//        }
        arrayData.distinctBy { it }
        binding.listEventos.layoutManager = LinearLayoutManager(context)
        binding.listEventos.adapter = AdapterCalendario(context, arrayData, arrayDatas)
    }

    fun showDay(context: Context, binding: ActivityCalendarioBinding, calendar: Calendar) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")

        val db = DatabaseHelper(context)
        val arrayData = db.selectAll(SimpleDateFormat("dd/MM/yyyy").format(calendar.time))
        val arrayDatas = ArrayList<String>()
        arrayData.sortBy { it.data_solitaria }
//        arrayData.forEach {
//            arrayDatas.add(it.data_solitaria)
//        }
        arrayData.distinctBy { it }
        var date = dateFormat.format(calendar.time)
        arrayDatas.add(date)
        binding.listDay.layoutManager = LinearLayoutManager(context)
        binding.listDay.adapter = AdapterItemsCalendario(context, arrayData)

        binding.listEventos.layoutManager = LinearLayoutManager(context)
        binding.listEventos.adapter = AdapterCalendario(context, arrayData, arrayDatas)
    }
}