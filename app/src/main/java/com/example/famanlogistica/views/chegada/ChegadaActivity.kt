package com.example.famanlogistica.views.chegada

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import com.example.famanlogistica.databinding.ActivityChegadaBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class ChegadaActivity : AppCompatActivity() {
    lateinit var binding: ActivityChegadaBinding
    lateinit var viewModel: ChegadaViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChegadaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(ChegadaViewModel::class.java)
        val editDate = binding.editHorario
        val editTime = binding.editTolerancia
        val btnSalvar = binding.btnSalvar
        val btnVoltar = binding.btnVoltar

        btnSalvar.setOnClickListener {
            viewModel.insertChegada(baseContext, binding)
        }

        btnVoltar.setOnClickListener {
            finish()
        }

        editDate.setOnClickListener {
            var data = Calendar.getInstance()
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this)

            datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
                val timePicker = TimePickerDialog(this, object: TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                        calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
                        editDate.setText(dateFormat.format(calendar.time))
                    }
                }, data.get(Calendar.HOUR_OF_DAY), data.get(Calendar.MINUTE), true)
                timePicker.create()
                timePicker.show()
            }
            datePicker.create()
            datePicker.show()
        }
        editTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(this, object: TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    val dateFormat = SimpleDateFormat("HH:mm")
                    editTime.setText(dateFormat.format(calendar.time))
                }
            }, 0, 0, true)
            timePicker.create()
            timePicker.show()
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