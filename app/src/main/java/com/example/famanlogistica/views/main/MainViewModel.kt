package com.example.famanlogistica.views.main

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.famanlogistica.adapters.AdapterEvento
import com.example.famanlogistica.classes.Clima
import com.example.famanlogistica.classes.Eventos
import com.example.famanlogistica.databinding.ActivityMainBinding
import com.example.famanlogistica.helpers.DatabaseHelper
import com.example.famanlogistica.helpers.Routes.URL_PREVISAO
import com.example.famanlogistica.views.calendario.CalendarioActivity
import com.example.famanlogistica.views.chegada.ChegadaActivity
import com.example.famanlogistica.views.pdf.PdfActivity
import com.example.famanlogistica.views.saida.SaidaActivity
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar

class MainViewModel : ViewModel() {
    fun loadData(context: Context, binding: ActivityMainBinding) {
        val sharedPrefs = context.getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val dateTime = sharedPrefs.getString("datetime", "")

        val cardCalendario = binding.cardCalendario
        val cardChegada = binding.cardChegada
        val cardSaida = binding.cardSaida
        val cardPdf = binding.cardPdf

        cardCalendario.setOnClickListener{ context.startActivity(Intent(context, CalendarioActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
        cardChegada.setOnClickListener{ context.startActivity(Intent(context, ChegadaActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
        cardSaida.setOnClickListener{ context.startActivity(Intent(context, SaidaActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
        cardPdf.setOnClickListener{ context.startActivity(Intent(context, PdfActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }

        getPrevisao(context, binding)
        //loadDefault(context, binding)
    }

    fun getEventos(context: Context) : ArrayList<Eventos> {
        val db = DatabaseHelper(context)
        val arrayList = db.selectRange(Calendar.getInstance(), 3)
        //val arrayList = db.selectEvento()
        return arrayList
    }

    fun getPrevisao(context: Context, binding: ActivityMainBinding) {
        val url = URL_PREVISAO
        val listEventos = binding.listDates
        listEventos.layoutManager = LinearLayoutManager(context)
        val arrayEventos = getEventos(context)
        var arrayClima = ArrayList<Clima>()
        val array = ArrayList<Clima>()
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val arrayClimas = JSONObject(response).getJSONArray("clima")
            for (i in 0 until arrayClimas.length()) {
                val json = arrayClimas.getJSONObject(i)
                var clima = Clima()
                clima.min = json.getInt("min")
                clima.max = json.getInt("max")
                clima.data = json.getString("data")
                clima.condicao = json.getString("condicao")
                clima.condicao_desc = json.getString("condicao_desc")
                clima.indice_uv = json.getInt("indice_uv")
                arrayClima.add(clima)
                array.add(clima)
            }
            arrayClima.forEach {
                var achou = false
                arrayEventos.forEach {item ->
                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    if (simpleDateFormat.format(dateFormat.parse(it.data)) == item.data_solitaria) achou = true
                }
                if (!achou) {
                    array.remove(it)
                }
            }
            listEventos.adapter = AdapterEvento(context, arrayEventos, array)
        }, { error ->
            Log.e("Request_previsao_error: ", error.toString())
            Snackbar.make(binding.root, "Houve um erro ao adquirir as informações de previsão do tempo!!", Snackbar.LENGTH_LONG).show()
            listEventos.adapter = AdapterEvento(context, arrayEventos, arrayClima)
        })
        Volley.newRequestQueue(context).add(stringRequest)
    }

//    private fun loadDefault(context: Context, binding: ActivityMainBinding) {
//
//        val listEvents = binding.listEvents
//        listEvents.layoutManager = LinearLayoutManager(context)
//        val arrayList = ArrayList<DefaultEvent>()
//        for (i in 0 until 10) {
//            val event = DefaultEvent(i, "13:00", "Titulo $i")
//            arrayList.add(event)
//        }
//        listEvents.adapter = AdapterEvento(context, arrayList)
//    }
}