package com.app.testkotlin.ui.tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.testkotlin.dto.ReportResponse
import com.app.testkotlin.model.Food
import com.app.testkotlin.repository.TrackingRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackingViewModel(private val trackingRepository: TrackingRepository) : ViewModel() {
    private val _report = MutableLiveData<ReportResponse?>()
    val report: MutableLiveData<ReportResponse?> get() = _report
    fun fetchReport(userId: Int, dateTime: String, reportType: String, authorization: String) {
        val call = trackingRepository.getTracking(userId, dateTime, reportType, authorization)
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

    fun deleteTracking(trackingId: Int, authorization: String) {
        val call = trackingRepository.deleteTracking(trackingId, authorization)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {

                } else {

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private val _msg = MutableLiveData<Boolean>()
    val msg: LiveData<Boolean> get() = _msg

    fun addTracking(userId: Int, foodId: Int, consumedGram: Double, token: String){
        trackingRepository.addTracking(userId, foodId, consumedGram, token).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _msg.value = true
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _msg.value = false            }
        })
    }



}