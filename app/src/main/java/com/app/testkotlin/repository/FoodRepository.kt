package com.app.testkotlin.repository

import com.app.testkotlin.api.ApiService
import com.app.testkotlin.model.Category
import com.app.testkotlin.model.Food
import retrofit2.Call

class FoodRepository(private val apiService: ApiService) {

    fun getAllFoodByCategoryId(cateId: Int,token: String): Call<List<Food>> {
        return apiService.getAllFoodByCategoryId(cateId,"Bearer $token")
    }
}