package com.app.testkotlin.ui.food

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.testkotlin.model.Category
import com.app.testkotlin.model.Food
import com.app.testkotlin.repository.CategoryRepository
import com.app.testkotlin.repository.FoodRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodViewModel(private val foodRepository: FoodRepository) : ViewModel() {
    private val _food = MutableLiveData<List<Food>?>()
    val food: MutableLiveData<List<Food>?> get() = _food

    fun fetchFood(cateId: Int, authorization: String) {
        val call = foodRepository.getAllFoodByCategoryId(cateId, authorization)
        call.enqueue(object : Callback<List<Food>> {
            override fun onResponse(call: Call<List<Food>>, response: Response<List<Food>>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    _food.value = result
                } else {
                }
            }

            override fun onFailure(call: Call<List<Food>>, t: Throwable) {
            }
        })
    }
}