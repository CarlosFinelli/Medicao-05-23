package com.example.famanlogistica.views.splash

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.famanlogistica.helpers.Routes.URL_TIMEZONE
import com.example.famanlogistica.views.main.MainActivity
import org.json.JSONObject

class SplashViewModel : ViewModel() {

    fun getTimeZone(context: Context) {
        val sharedPrefs = context.getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        Thread {
            val url = URL_TIMEZONE
            val stringRequest = StringRequest(Request.Method.GET, url, { response ->
                val json = JSONObject(response)
                val dateTime = json.getString("datetime")
                sharedPrefs.edit().putString("dateTime", dateTime).apply()
                Handler().postDelayed({
                    context.startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }, 2000)
            }, { error ->
                Log.e("Request_error: ", error.toString())
                Toast.makeText(context, "Houve um erro ao recuperar as informações de horário!", Toast.LENGTH_LONG).show()
                Handler().postDelayed({
                    context.startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

                }, 2000)
            })
            Volley.newRequestQueue(context).add(stringRequest)
        }.start()
    }
}