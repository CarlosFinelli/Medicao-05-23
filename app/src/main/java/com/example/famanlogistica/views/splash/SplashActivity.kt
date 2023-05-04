package com.example.famanlogistica.views.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.example.famanlogistica.R
import com.example.famanlogistica.views.main.MainActivity

class SplashActivity : AppCompatActivity() {
    lateinit var viewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        viewModel.getTimeZone(baseContext)
    }
}