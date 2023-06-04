package com.app.testkotlin.repository

import com.app.testkotlin.api.ApiService
import com.app.testkotlin.dto.ForgotRequestUsernameOrEmail
import retrofit2.Call
import retrofit2.Callback

class ForgotRepository(private val apiService: ApiService) {
    fun forgot(forgotRequestUsernameOrEmail: ForgotRequestUsernameOrEmail): Call<Void> {
        return apiService.forgot(forgotRequestUsernameOrEmail)
    }
}