package com.app.testkotlin.repository

import com.app.testkotlin.api.ApiService
import com.app.testkotlin.model.Food
import retrofit2.Call

class FoodDetailRepository(private val apiService: ApiService) {
    fun getFoodDetailById(foodId: Int, token: String): Call<Food> {
        return apiService.getFoodDetailById(foodId, "Bearer $token")
    }
}