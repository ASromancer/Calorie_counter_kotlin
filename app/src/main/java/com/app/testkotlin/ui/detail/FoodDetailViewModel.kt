package com.app.testkotlin.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.testkotlin.model.Food
import com.app.testkotlin.repository.FoodDetailRepository
import com.app.testkotlin.repository.TrackingRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodDetailViewModel(private val foodDetailRepository: FoodDetailRepository) : ViewModel() {
    private val _foodDetail = MutableLiveData<Food>()
    val foodDetail: LiveData<Food> get() = _foodDetail

    fun fetchAccountByUsername(foodId: Int?, token: String?) {
        if (foodId != null) {
            if (token != null) {
                foodDetailRepository.getFoodDetailById(foodId, token).enqueue(object :
                    Callback<Food> {
                    override fun onResponse(call: Call<Food>, response: Response<Food>) {
                        if (response.isSuccessful) {
                            _foodDetail.value = response.body()
                        } else {
                            // Handle error
                        }
                    }

                    override fun onFailure(call: Call<Food>, t: Throwable) {
                        // Handle failure
                    }
                })
            }
        }
    }
}