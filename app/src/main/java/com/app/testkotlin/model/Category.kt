package com.app.testkotlin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Category(
    @field:Expose @field:SerializedName("id") var id: Int,
    @field:Expose @field:SerializedName(
        "name"
    ) var name: String,
    @field:Expose @field:SerializedName("image") var image: String,
    foods: List<Food>
) {

    @SerializedName("foods")
    @Expose
    private var foods: List<Food>

    init {
        this.foods = foods
    }

    fun getFoods(): List<Food> {
        return foods
    }

    fun setFoods(foods: List<Food>) {
        this.foods = foods
    }
}