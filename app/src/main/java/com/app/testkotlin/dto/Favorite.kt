package com.app.testkotlin.dto

import com.app.testkotlin.model.Food
import com.app.testkotlin.model.User

class Favorite(var id: Int, user: User, food: Food) {
    private var user: User
    private var food: Food

    init {
        this.user = user
        this.food = food
    }

    fun getFood(): Food {
        return food
    }

    fun setFood(food: Food) {
        this.food = food
    }

    fun getUser(): User {
        return user
    }

    fun setUser(user: User) {
        this.user = user
    }
}