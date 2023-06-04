package com.app.testkotlin.ui.forgot

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.testkotlin.dto.ForgotRequestUsernameOrEmail
import com.app.testkotlin.repository.ForgotRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotViewModel(private val forgotRepository: ForgotRepository) : ViewModel() {
    private val _msg = MutableLiveData<Boolean>()
    val msg: MutableLiveData<Boolean> get() = _msg

    fun forgot(forgotRequestUsernameOrEmail: ForgotRequestUsernameOrEmail) {
        val call = forgotRepository.forgot(forgotRequestUsernameOrEmail)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _msg.value = response.code() == 200
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _msg.value = false
            }
        })
    }
}