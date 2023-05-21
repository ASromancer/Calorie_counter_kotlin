package com.app.testkotlin.di

import com.app.testkotlin.api.ApiService
import com.app.testkotlin.utils.AppConstants.BASE_URL
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// APIModule.kt
import org.koin.dsl.module

val apiModule = module {
    single { createAPIService(get()) }
}

private fun createAPIService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}
