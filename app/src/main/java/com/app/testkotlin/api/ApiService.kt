package com.app.testkotlin.api

import com.app.testkotlin.dto.Favorite
import com.app.testkotlin.dto.FoodTrackingHistory
import com.app.testkotlin.dto.LoginRequest
import com.app.testkotlin.dto.LoginResponse
import com.app.testkotlin.dto.Password
import com.app.testkotlin.dto.ReportResponse
import com.app.testkotlin.dto.UserInfo
import com.app.testkotlin.model.Account
import com.app.testkotlin.model.Category
import com.app.testkotlin.model.Food
import com.app.testkotlin.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("categories/all")
    fun getAllCategory(@Header("Authorization") token: String?): Call<List<Category>>

    @GET("favfoods/{userId}")
    fun getFavoriteFoodByUserId(
        @Path("userId") userId: Int,
        @Header("Authorization") token: String?
    ): Call<List<Favorite>>

    @GET("accounts/{username}")
    fun getAccountByUsername(
        @Path("username") username: String?,
        @Header("Authorization") token: String?
    ): Call<Account>

    @PUT("accounts/changePwd")
    fun changePassword(
        @Header("Authorization") token: String?,
        @Body password: Password?
    ): Call<Void>

    @GET("users/{id}")
    fun getUserById(
        @Header("Authorization") token: String?,
        @Path("id") id: Int
    ): Call<User>

    @PUT("users/update")
    @Multipart
    fun putUserData(
        @Header("Authorization") token: String?,
        @Part("user") userJson: RequestBody?
    ): Call<Void>

    @Multipart
    @PUT("users/update")
    fun uploadAvatar(
        @Header("Authorization") token: String?,
        @Part("user") userJson: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Call<Void>

    @GET("foods/all/category/{cateId}")
    fun getAllFoodByCategoryId(
        @Path("cateId") categoryId: Int,
        @Header("Authorization") token: String?
    ): Call<List<Food>>

    @GET("foods/{id}")
    fun getFoodDetailById(
        @Path("id") foodId: Int,
        @Header("Authorization") token: String?
    ): Call<Food>

    @GET("/api/v1/tracking/user/{userId}")
    fun getTrackingList(
        @Path("userId") userId: Int,
        @Header("Authorization") token: String?
    ): Call<List<FoodTrackingHistory>>

    @POST("/api/v1/tracking/add/{userId}/{foodId}/{consumedGram}")
    fun addTracking(
        @Path("userId") userId: Int,
        @Path("foodId") foodId: Int,
        @Path("consumedGram") consumedGram: Double?,
        @Header("Authorization") token: String?
    ): Call<Void>

    @DELETE("/api/v1/tracking/remove/{trackingId}")
    fun deleteTracking(
        @Path("trackingId") trackingId: Int,
        @Header("Authorization") token: String?
    ): Call<Void>

    @GET("tracking/report")
    fun getReport(
        @Query("userId") userId: Int?,
        @Query("dateTime") dateTime: String?,
        @Query("reportType") reportType: String?,
        @Header("Authorization") token: String?
    ): Call<ReportResponse>

    @POST("auth/register")
    fun signup(@Body userInfo: UserInfo): Call<Void>
}