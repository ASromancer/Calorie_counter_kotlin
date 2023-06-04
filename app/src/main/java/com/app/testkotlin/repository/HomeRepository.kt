package com.app.testkotlin.repository

import com.app.testkotlin.api.ApiService
import com.app.testkotlin.dto.ReportResponse
import retrofit2.Call

class HomeRepository(private val apiService: ApiService) {
    fun getTracking(userId: Int, dateTime: String, reportType: String, token: String): Call<ReportResponse> {
        return apiService.getReport(userId, dateTime, reportType, "Bearer $token")
    }
}