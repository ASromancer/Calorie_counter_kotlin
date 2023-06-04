package com.app.testkotlin.repository

import com.app.testkotlin.api.ApiService
import com.app.testkotlin.dto.UserInfo
import retrofit2.Call

class RegisterRepository(private val apiService: ApiService) {
    fun register(userInfo: UserInfo): Call<Void> {
        return apiService.signup(userInfo)
    }
}