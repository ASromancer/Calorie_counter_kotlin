package com.app.testkotlin.repository

import com.app.testkotlin.api.ApiService
import com.app.testkotlin.dto.Favorite
import com.app.testkotlin.dto.LoginRequest
import com.app.testkotlin.dto.LoginResponse
import com.app.testkotlin.model.Account
import retrofit2.Call

class LoginRepository(private val apiService: ApiService) {
    suspend fun login(username: String, password: String): LoginResponse {
        val loginRequest = LoginRequest(username, password)
        return apiService.login(loginRequest)
    }

    fun getAccountByUsername(username: String, token: String): Call<Account> {
        return apiService.getAccountByUsername(username, "Bearer $token")
    }
}