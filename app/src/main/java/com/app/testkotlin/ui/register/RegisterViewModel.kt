package com.app.testkotlin.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.testkotlin.dto.ReportResponse
import com.app.testkotlin.dto.UserInfo
import com.app.testkotlin.repository.HomeRepository
import com.app.testkotlin.repository.RegisterRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {
    private val _msg = MutableLiveData<Boolean>()
    val msg: MutableLiveData<Boolean> get() = _msg

    fun register(userInfo: UserInfo) {
        val call = registerRepository.register(userInfo)
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