package com.app.testkotlin.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.testkotlin.dto.Password
import com.app.testkotlin.model.Account
import com.app.testkotlin.model.User
import com.app.testkotlin.repository.ProfileRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

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

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user
    fun getUserById(token: String, id: Int) {
        profileRepository.getUserById(token, id).enqueue(object :
            Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
            }
        })
    }

    private val _msg = MutableLiveData<Boolean>()
    val msg: MutableLiveData<Boolean> get() = _msg
    fun putUserData(token: String, userJson: RequestBody) {
        val call = profileRepository.putUserData(token, userJson)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _msg.value = response.code() == 200
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _msg.value = false
            }
        })
    }

    private val _msgImage = MutableLiveData<Boolean>()
    val msgImage: MutableLiveData<Boolean> get() = _msgImage
    fun uploadAvatar(token: String, userJson: RequestBody, image: MultipartBody.Part) {
        val call = profileRepository.uploadAvatar(token, userJson, image)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _msgImage.value = response.code() == 200
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
                _msgImage.value = false
            }
        })
    }


}

