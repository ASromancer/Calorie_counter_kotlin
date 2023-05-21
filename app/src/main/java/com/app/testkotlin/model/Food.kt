package com.app.testkotlin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Food : Serializable {
    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("protein")
    @Expose
    var protein = 0.0

    @SerializedName("fat")
    @Expose
    var fat = 0.0

    @SerializedName("carb")
    @Expose
    var carb = 0.0

    @SerializedName("energyPerServing")
    var energyPerServing = 0.0

    constructor() {}
    constructor(
        id: Int,
        name: String?,
        description: String?,
        image: String?,
        protein: Double,
        fat: Double,
        carb: Double,
        energyPerServing: Double
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.image = image
        this.protein = protein
        this.fat = fat
        this.carb = carb
        this.energyPerServing = energyPerServing
    }
}