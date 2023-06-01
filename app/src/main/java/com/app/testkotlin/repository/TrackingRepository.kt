package com.app.testkotlin.repository

import com.app.testkotlin.api.ApiService
import com.app.testkotlin.dto.FoodTrackingHistory
import com.app.testkotlin.dto.ReportResponse
import retrofit2.Call

class TrackingRepository(private val apiService: ApiService) {

    fun getTrackingList(userId: Int, token: String): Call<List<FoodTrackingHistory>> {
        return apiService.getTrackingList(userId,"Bearer $token" )
    }

    fun addTracking(userId: Int, foodId: Int, consumedGram: Double, token: String): Call<Void> {
        return apiService.addTracking(userId, foodId, consumedGram,"Bearer $token" )
    }

    fun deleteTracking(trackingId: Int,token: String): Call<Void> {
        return apiService.deleteTracking(trackingId,"Bearer $token" )
    }

    fun getTracking(userId: Int, dateTime: String, reportType: String, token: String): Call<ReportResponse> {
        return apiService.getReport(userId, dateTime, reportType, "Bearer $token")
    }

}