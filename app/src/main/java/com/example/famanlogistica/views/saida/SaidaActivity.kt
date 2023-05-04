package com.example.famanlogistica.views.saida

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import com.example.famanlogistica.R
import com.example.famanlogistica.databinding.ActivitySaidaBinding
import com.example.famanlogistica.views.chegada.ChegadaViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class SaidaActivity : AppCompatActivity() {
    lateinit var binding: ActivitySaidaBinding
    lateinit var viewModel: SaidaViewModel
    var vazio = false
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySaidaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(SaidaViewModel::class.java)
        val editDate = binding.editHorario
        val editTime = binding.editTolerancia
        val btnSalvar = binding.btnSalvar
        val btnVoltar = binding.btnVoltar

        btnSalvar.setOnClickListener {
            if (!validateFields()) viewModel.insertSaida(baseContext, binding)
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
                }, 0, 0, true)
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

    fun validateFields() : Boolean {
        vazio = false
        val editTitle = binding.editTitle
        val editDesc = binding.editDescricao
        val editDate = binding.editHorario
        val editTime = binding.editTolerancia
        isEmpty(editTime)
        isEmpty(editTitle)
        isEmpty(editDate)
        isEmpty(editDesc)
        return vazio
    }

    fun isEmpty(editText: EditText) {
        if (editText.text.toString().isBlank()) {
            editText.error = "Este campo não deve estar vazio"
            vazio = true
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