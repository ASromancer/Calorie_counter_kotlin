package com.app.testkotlin.dto

import com.google.gson.annotations.SerializedName

class ReportResponse {
    @SerializedName("columnData")
    val columnData: Map<String, Float>? = null

    @SerializedName("max")
    val max: Double? = null

    @SerializedName("min")
    val min: Double? = null

    @SerializedName("average")
    val average: Double? = null

    @SerializedName("total")
    val total: Double? = null

    @SerializedName("consumedHistory")
    val consumedHistory: List<FoodTrackingHistory>? = null
}