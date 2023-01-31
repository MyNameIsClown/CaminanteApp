package com.carrasco.caminante.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.carrasco.caminante.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}