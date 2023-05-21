package com.app.testkotlin.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.testkotlin.dto.Favorite
import com.app.testkotlin.repository.FavoriteRepository
import com.app.testkotlin.repository.LoginRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {
    private val _favoriteFoods = MutableLiveData<List<Favorite>>()
    val favoriteFoods: LiveData<List<Favorite>> get() = _favoriteFoods

    fun fetchFavoriteFoods(userId: Int, token: String?) {
        if (token != null) {
            favoriteRepository.getFavoriteFood(userId, token).enqueue(object :
                Callback<List<Favorite>> {
                override fun onResponse(call: Call<List<Favorite>>, response: Response<List<Favorite>>) {
                    if (response.isSuccessful) {
                        _favoriteFoods.value = response.body()
                    } else {
                        // Handle error
                    }
                }

                override fun onFailure(call: Call<List<Favorite>>, t: Throwable) {
                    // Handle failure
                }
            })
        }
    }
}