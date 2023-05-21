package com.app.testkotlin.api

import com.app.testkotlin.dto.Favorite
import com.app.testkotlin.dto.LoginRequest
import com.app.testkotlin.dto.LoginResponse
import com.app.testkotlin.model.Account
import com.app.testkotlin.model.Category
import com.app.testkotlin.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


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
}