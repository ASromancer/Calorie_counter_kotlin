package com.app.testkotlin.ui.forgot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.app.testkotlin.databinding.ActivityForgotBinding
import com.app.testkotlin.dto.ForgotRequestUsernameOrEmail
import com.app.testkotlin.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class ForgotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotBinding
    private val forgotViewModel: ForgotViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setControl()
    }

    private fun setControl() {
        binding.btnResetPassword.setOnClickListener {
            val usernameOrPassword: String = binding.edtEmail.text.toString().trim()
            if (usernameOrPassword == ""){
                Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
            }
            else{
                forgotViewModel.forgot(ForgotRequestUsernameOrEmail(usernameOrPassword))
                forgotViewModel.msg.observe(this){
                    if(it == true){
                        Toast.makeText(this, "Reset Password Success!", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Please check your email!", Toast.LENGTH_SHORT).show()
                    }

                    val handler = Handler()
                    handler.postDelayed({
                        val loginIntent = Intent(this, LoginActivity::class.java)
                        startActivity(loginIntent)
                    }, 1000)
                }
            }

        }
    }
}