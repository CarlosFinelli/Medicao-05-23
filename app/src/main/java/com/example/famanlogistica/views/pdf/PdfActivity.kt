package com.example.famanlogistica.views.pdf

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.famanlogistica.R
import com.example.famanlogistica.databinding.ActivityPdfBinding
import com.example.famanlogistica.helpers.DatabaseHelper
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar


class PdfActivity : AppCompatActivity() {
    lateinit var binding: ActivityPdfBinding
    lateinit var viewModel: PdfViewModel

    var pageHeight = 1120
    var pagewidth = 792
    var bmp: Bitmap? = null
    var scaledbmp: Bitmap? = null
    var PERMISSION_REQUEST_CODE = 200
    var selected = 0

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
            if(checkPermission()) generatePDF()
            else requestPermission()
        }

        btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun generatePDF() {
        val db = DatabaseHelper(baseContext)

        val data = binding.editDate.text.toString()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy").parse(data)
        val formatedDate = SimpleDateFormat("dd-MM-yyyy").format(dateFormat)
        val tipo = binding.editTipo.text.toString()
        var nome = "Relatorio_${formatedDate}_$tipo.pdf"

        val array = if (selected == 0) {
            db.selectEventoChegada()
        } else if (selected == 2) {
            db.selectEvento()
        } else {
            db.selectEventoSaida()
        }
        array.removeIf { it.data_solitaria != data }
        array.sortBy{it.data_solitaria}

        val pdfDocument = PdfDocument()

        val paint = Paint()
        val title = Paint()

        val mypageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()

        val myPage = pdfDocument.startPage(mypageInfo)

        val canvas = myPage.canvas


        //canvas.drawBitmap(scaledbmp!!, 56f, 40f, paint)

        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        title.textSize = 24f
        title.color = ContextCompat.getColor(this, R.color.black)
        title.textAlign = Paint.Align.CENTER

        canvas.drawText("FaMan Logística", 396f, 80f, title)

        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        title.textSize = 20f
        canvas.drawText(data, 396f, 130f, title)

        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(this, R.color.black)
        title.textSize = 20f

        var largura = 396f
        var altura = 360f
        title.textAlign = Paint.Align.CENTER
        array.forEach {

            canvas.drawText("${if(it.tipo == 1) "Chegada" else "Saída"} - ${it.data} - ${it.descricao}", largura, altura, title)
            //largura += 30f
            altura += 40f
        }

        pdfDocument.finishPage(myPage)

        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), nome)
        try {
            pdfDocument.writeTo(FileOutputStream(file))

            Snackbar.make(
                binding.root,
                "PDF criado com sucesso. Diretório: ${file.absolutePath}",
                Snackbar.LENGTH_LONG
            ).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        pdfDocument.close()
    }

    private fun checkPermission(): Boolean {
        // checking of permissions.
        val permission1 =
            ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permissão garantida..", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permissão negada.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun showAlert(editTipo: EditText) {
        val array = arrayOf("Chegada", "Saída", "Ambos")
        var selecionado = 0
        AlertDialog.Builder(this)
            .setTitle("Escolha o tipo da informação que será exportada")
            .setIcon(com.example.famanlogistica.R.drawable.outline_edit)
            //.setMessage("Escolha o tipo da informação que será exportada")
            .setSingleChoiceItems(array, -1, DialogInterface.OnClickListener { dialog, which ->
                selecionado = which
                selected = which
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