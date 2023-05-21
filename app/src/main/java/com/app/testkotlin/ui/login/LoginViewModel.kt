package com.app.testkotlin.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.testkotlin.dto.LoginResponse
import com.app.testkotlin.model.Account
import com.app.testkotlin.model.Category
import com.app.testkotlin.repository.LoginRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = loginRepository.login(username, password)
                _loginResult.value = Result.success(response)
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

    private val _account = MutableLiveData<Account>()
    val account: LiveData<Account> get() = _account

    fun fetchAccountByUsername(username: String?, token: String?) {
        if (username != null) {
            if (token != null) {
                loginRepository.getAccountByUsername(username, token).enqueue(object : Callback<Account> {
                    override fun onResponse(call: Call<Account>, response: Response<Account>) {
                        if (response.isSuccessful) {
                            _account.value = response.body()
                        } else {
                            // Handle error
                        }
                    }

                    override fun onFailure(call: Call<Account>, t: Throwable) {
                        // Handle failure
                    }
                })
            }
        }
    }


}
