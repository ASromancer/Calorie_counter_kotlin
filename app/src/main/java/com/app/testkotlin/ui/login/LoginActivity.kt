package com.app.testkotlin.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.app.testkotlin.MainActivity
import com.app.testkotlin.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide();
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edtUsername.setText("hiep")
        binding.edtPassword.setText("31100102")
        binding.pgBar.visibility = View.GONE

        binding.btnLogin.setOnClickListener {
            binding.pgBar.visibility = View.VISIBLE
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            loginViewModel.login(username, password)
        }

        loginViewModel.loginResult.observe(this) { result ->
            if (result.isSuccess) {
                val loginResponse = result.getOrNull()
                if (loginResponse != null) {
                    val token = loginResponse.token
                    loginViewModel.account.observe(this) { account ->
                        val sharedPref: SharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPref.edit()

                        editor.putString("token", token)
                        account.user?.let { editor.putInt("userId", it.id) }
                        editor.apply()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    loginViewModel.fetchAccountByUsername(binding.edtUsername.text.toString().trim(), token)
                }

            } else {
                val exception = result.exceptionOrNull()
                if (exception != null) {
                    Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }


}