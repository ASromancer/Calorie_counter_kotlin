package com.app.testkotlin.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.testkotlin.model.Account
import com.app.testkotlin.model.Category
import com.app.testkotlin.repository.CategoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {
    private val _category = MutableLiveData<List<Category>?>()
    val category: MutableLiveData<List<Category>?> get() = _category

    fun fetchCategory(authorization: String) {
        val call = categoryRepository.getAllCategory(authorization)
        call.enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    _category.value = result
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
