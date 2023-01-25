package com.carrasco.caminante.ui.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.carrasco.caminante.MainActivity
import com.carrasco.caminante.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            navigate("Sesion iniciada")
        }

        binding.registerButton.setOnClickListener{
            navigate("Registrando")
        }
    }

    fun navigate (msg: String){
        val username = binding.usernameInput.text
        val email = binding.emailInput.text
        val password = binding.passwordInput.text

        if (username.isEmpty()||email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Error, alguno de los campos esta vacio", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }
}