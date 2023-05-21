package com.app.testkotlin.repository

import com.app.testkotlin.api.ApiService
import com.app.testkotlin.model.Category
import retrofit2.Call

class CategoryRepository(private val apiService: ApiService) {
    fun getAllCategory(token: String): Call<List<Category>> {
        return apiService.getAllCategory("Bearer $token")
    }
}
