package com.app.testkotlin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.testkotlin.dto.ReportResponse
import com.app.testkotlin.repository.HomeRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _report = MutableLiveData<ReportResponse?>()
    val report: MutableLiveData<ReportResponse?> get() = _report
    fun fetchReport(userId: Int, dateTime: String, reportType: String, authorization: String) {
        val call = homeRepository.getTracking(userId, dateTime, reportType, authorization)
        call.enqueue(object : Callback<ReportResponse> {
            override fun onResponse(call: Call<ReportResponse>, response: Response<ReportResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    _report.value = result
                } else {
                    _report.value = null
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}