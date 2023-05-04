package com.example.famanlogistica.views.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.famanlogistica.R
import com.example.famanlogistica.databinding.ActivityMainBinding
import com.example.famanlogistica.views.calendario.CalendarioActivity
import com.example.famanlogistica.views.chegada.ChegadaActivity
import com.example.famanlogistica.views.pdf.PdfActivity
import com.example.famanlogistica.views.saida.SaidaActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.loadData(baseContext, binding)


    }
}