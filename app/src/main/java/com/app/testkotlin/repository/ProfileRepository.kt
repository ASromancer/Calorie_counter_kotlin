package com.app.testkotlin.repository

import com.app.testkotlin.api.ApiService
import com.app.testkotlin.dto.Password
import com.app.testkotlin.model.Account
import com.app.testkotlin.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT


class ProfileRepository(private val apiService: ApiService) {
    fun getAccountByUsername(username: String, token: String): Call<Account> {
        return apiService.getAccountByUsername(username, "Bearer $token")
    }

    fun changePassword(token: String, password: Password): Call<Void> {
        return apiService.changePassword( "Bearer $token", password)
    }

    fun getUserById(token: String, id: Int): Call<User> {
        return apiService.getUserById( "Bearer $token", id)
    }

    fun putUserData(token: String, userJson: RequestBody): Call<Void>{
        return apiService.putUserData("Bearer $token", userJson)
    }

    fun uploadAvatar(token: String, userJson: RequestBody, image: MultipartBody.Part): Call<Void>{
        return apiService.uploadAvatar("Bearer $token", userJson, image)
    }
}