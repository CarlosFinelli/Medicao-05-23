package com.example.famanlogistica.views.pdf

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.famanlogistica.R
import com.example.famanlogistica.databinding.ActivityPdfBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class PdfActivity : AppCompatActivity() {
    lateinit var binding: ActivityPdfBinding
    lateinit var viewModel: PdfViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPdfBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(PdfViewModel::class.java)
        val editDate = binding.editDate
        val editTipo = binding.editTipo
        val btnSalvar = binding.btnSalvar
        val btnVoltar = binding.btnVoltar

        editDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this)
            datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                editDate.setText(dateFormat.format(calendar.time))
            }
            datePicker.create()
            datePicker.show()
        }

        editTipo.setOnClickListener { showAlert(editTipo) }

        btnSalvar.setOnClickListener {
            finish()
        }

        btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun showAlert(editTipo: EditText) {
        val array = arrayOf("Chegada", "Saída", "Ambos")
        var selecionado = 0
        AlertDialog.Builder(this)
            .setTitle("Escolha o tipo da informação que será exportada")
            .setIcon(R.drawable.outline_edit)
            //.setMessage("Escolha o tipo da informação que será exportada")
            .setSingleChoiceItems(array, -1, DialogInterface.OnClickListener { dialog, which ->
                selecionado = which
            })
            .setPositiveButton("Finalizar", DialogInterface.OnClickListener { dialog, which ->
                editTipo.setText(array[selecionado])
                dialog.dismiss()
            })
            .setNeutralButton("Cancelar", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .create()
            .show()
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