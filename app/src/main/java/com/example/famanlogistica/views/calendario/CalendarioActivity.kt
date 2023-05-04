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
        val mainLayout = binding.mainLayout
        val calendario = binding.calendario
        calendario.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val simpleDateFormat = SimpleDateFormat("dd MMMM ")
            val date = Calendar.getInstance()
            date.set(year, month, dayOfMonth)
            mainLayout.alpha = 0.5f
            binding.cardDialog.visibility = View.VISIBLE
            binding.textTitle.setText(simpleDateFormat.format(date.time))
        }

        binding.btnClose.setOnClickListener {
            binding.cardDialog.visibility = View.GONE
            mainLayout.alpha = 1f
        }
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