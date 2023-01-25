package com.carrasco.caminante

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.carrasco.caminante.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}