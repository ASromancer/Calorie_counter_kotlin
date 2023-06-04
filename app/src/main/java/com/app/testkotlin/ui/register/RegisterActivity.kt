package com.app.testkotlin.ui.register

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.app.testkotlin.databinding.ActivityRegisterBinding
import com.app.testkotlin.dto.UserInfo
import com.app.testkotlin.ui.login.LoginActivity
import com.app.testkotlin.utils.AppConstants
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModel()
    var username: String = ""
    var password: String = ""
    var email: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var confirmPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setControl()
    }

    private fun setControl() {
        binding.btnSignUp.setOnClickListener {
            username = binding.edtUsername.text.toString().trim()
            password = binding.edtPassword.text.toString().trim()
            email = binding.edtEmail.text.toString().trim()
            firstName = binding.edtFirstname.text.toString().trim()
            lastName = binding.edtLastname.text.toString().trim()
            confirmPassword = binding.edtConfirmPassword.text.toString().trim()
            if(username == "" || password == "" || confirmPassword == "" || firstName == "" || lastName == "" || email == ""){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            else if(password != confirmPassword){
                Toast.makeText(this, "Confirm password is not similar to password", Toast.LENGTH_SHORT).show()
            }
            else{
                binding.signupProgress.visibility = View.VISIBLE
                val userInfo = UserInfo(username, firstName, email, password, lastName, AppConstants.ROLE_USER)
                registerViewModel.register(userInfo)
                registerViewModel.msg.observe(this){
                    if (it == true){
                        binding.signupProgress.visibility = View.GONE
                        Toast.makeText(this, "Register Success!", Toast.LENGTH_SHORT).show()
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