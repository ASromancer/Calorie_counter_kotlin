package com.app.testkotlin.di

import com.app.testkotlin.utils.AppConstants.BASE_URL
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// RetrofitModule.kt
import org.koin.dsl.module

//BASE_URL = "https://calories-tracking-app.azurewebsites.net/api/v1/"
val retrofitModule = module {
    single { createRetrofit() }
}

private fun createRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

