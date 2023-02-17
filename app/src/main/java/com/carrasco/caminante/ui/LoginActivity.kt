package com.carrasco.caminante.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.carrasco.caminante.R
import com.carrasco.caminante.data.dao.UserDao
import com.carrasco.caminante.data.model.Publication
import com.carrasco.caminante.data.model.User
import com.carrasco.caminante.databinding.ActivityLoginBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1000)
        setTheme(R.style.Theme_Caminante)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Analytics
        val analytics = FirebaseAnalytics.getInstance(this)

        binding.registerButton.setOnClickListener{
            val email = binding.emailInput.text
            val password = binding.passwordInput.text
            if(email.isNotEmpty() && password.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    email.toString(), password.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        UserDao.save(
                            User(email.toString(), mutableListOf())
                        )
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    }else{
                        showAlert()
                    }
                }
            }
        }
        binding.loginButton.setOnClickListener{
            val email = binding.emailInput.text
            val password = binding.passwordInput.text
            if(email.isNotEmpty() && password.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    email.toString(), password.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    }else{
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al autenticar al usuario")
        builder.setPositiveButton("Acceptar", null)
        builder.create().show()
    }


}