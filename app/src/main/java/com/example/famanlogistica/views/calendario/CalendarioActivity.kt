package com.example.famanlogistica.views.calendario

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CalendarView
import androidx.lifecycle.ViewModelProvider
import com.example.famanlogistica.R
import com.example.famanlogistica.databinding.ActivityCalendarioBinding
import com.google.android.material.resources.TextAppearance
import java.text.SimpleDateFormat
import java.util.Calendar

class CalendarioActivity : AppCompatActivity() {
    lateinit var binding: ActivityCalendarioBinding
    lateinit var viewModel: CalendarioViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCalendarioBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(CalendarioViewModel::class.java)
        viewModel.setComponents(baseContext, binding)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {

            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}