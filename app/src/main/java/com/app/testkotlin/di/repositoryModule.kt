package com.app.testkotlin.di

import com.app.testkotlin.repository.CategoryRepository
import com.app.testkotlin.repository.FavoriteRepository
import com.app.testkotlin.repository.FoodDetailRepository
import com.app.testkotlin.repository.FoodRepository
import com.app.testkotlin.repository.HomeRepository
import com.app.testkotlin.repository.LoginRepository
import com.app.testkotlin.repository.ProfileRepository
import com.app.testkotlin.repository.RegisterRepository
import com.app.testkotlin.repository.TrackingRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { CategoryRepository(get()) }
    single { LoginRepository(get()) }
    single { FavoriteRepository(get()) }
    single { FoodRepository(get()) }
    single { FoodDetailRepository(get()) }
    single { TrackingRepository(get()) }
    single { ProfileRepository(get()) }
    single { HomeRepository(get()) }
    single { RegisterRepository(get()) }
}




