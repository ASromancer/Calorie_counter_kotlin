package com.app.testkotlin.repository

import com.app.testkotlin.api.ApiService
import com.app.testkotlin.dto.Password
import com.app.testkotlin.model.Account
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
}