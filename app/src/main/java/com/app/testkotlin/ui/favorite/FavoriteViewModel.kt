package com.app.testkotlin.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.testkotlin.dto.Favorite
import com.app.testkotlin.dto.UserInfo
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

    private val _msg = MutableLiveData<Boolean>()
    val msg: MutableLiveData<Boolean> get() = _msg

    fun deleteFoodFromFavorite(userId: Int, foodId: Int, token: String) {
        val call = favoriteRepository.deleteFoodFromFavorite(userId, foodId, token)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _msg.value = response.code() == 200
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _msg.value = false
            }
        })
    }

    private val _addMsg = MutableLiveData<Boolean>()
    val addMsg: MutableLiveData<Boolean> get() = _addMsg

    fun addFoodToFavorite(userId: Int, foodId: Int, token: String) {
        val call = favoriteRepository.addFoodToFavorite(userId, foodId, token)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _addMsg.value = response.code() == 200
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _addMsg.value = false
            }
        })
    }
}