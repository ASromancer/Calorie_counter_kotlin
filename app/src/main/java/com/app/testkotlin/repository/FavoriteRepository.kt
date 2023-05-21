package com.app.testkotlin.repository

import com.app.testkotlin.api.ApiService
import com.app.testkotlin.dto.Favorite
import com.app.testkotlin.model.Category
import retrofit2.Call

class FavoriteRepository(private val apiService: ApiService) {
    fun getFavoriteFood(userId: Int, token: String): Call<List<Favorite>> {
        return apiService.getFavoriteFoodByUserId(userId, "Bearer $token")
    }
}

