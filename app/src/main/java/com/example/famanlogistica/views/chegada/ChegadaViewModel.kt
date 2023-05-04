package com.example.famanlogistica.views.chegada

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.famanlogistica.classes.Eventos
import com.example.famanlogistica.databinding.ActivityChegadaBinding
import com.example.famanlogistica.helpers.DatabaseHelper
import com.example.famanlogistica.views.main.MainActivity
import com.google.android.material.snackbar.Snackbar

class ChegadaViewModel : ViewModel() {

    fun insertChegada(context: Context, binding: ActivityChegadaBinding) {
        val db = DatabaseHelper(context)
        val evento = convertEvento(binding)
        if (db.insertChegada(evento) != -1L) {
            context.startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            Toast.makeText(context, "Sucesso ao inserir as informações!!", Toast.LENGTH_LONG).show()
        } else {
            Snackbar.make(binding.root, "Houve um erro ao inserir a sua saída!!", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun convertEvento(binding: ActivityChegadaBinding): Eventos {
        val evento = Eventos()
        evento.titulo = binding.editTitle.text.toString()
        evento.descricao = binding.editDescricao.text.toString()
        evento.data = binding.editHorario.text.toString()
        evento.tolerancia = binding.editTolerancia.text.toString()
        return evento
    }
}