package com.app.testkotlin.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.testkotlin.dto.Password
import com.app.testkotlin.model.Account
import com.app.testkotlin.repository.ProfileRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {
    private val _account = MutableLiveData<Account>()
    val account: LiveData<Account> get() = _account

    fun fetchAccountByUsername(username: String?, token: String?) {
        if (username != null) {
            if (token != null) {
                profileRepository.getAccountByUsername(username, token).enqueue(object :
                    Callback<Account> {
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

    private val _changePasswordResponse = MutableLiveData<Boolean>()
    val changePasswordResponse: LiveData<Boolean> get() = _changePasswordResponse

    fun changePassword(token: String, password: Password) {
        profileRepository.changePassword(token, password).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200) {
                    _changePasswordResponse.value = true
                } else {
                    _changePasswordResponse.value = false
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _changePasswordResponse.value = false
            }
        })
    }
}

