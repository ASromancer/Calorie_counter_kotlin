package com.app.testkotlin.di

import com.app.testkotlin.ui.detail.FoodDetailViewModel
import com.app.testkotlin.ui.category.CategoryViewModel
import com.app.testkotlin.ui.favorite.FavoriteViewModel
import com.app.testkotlin.ui.food.FoodViewModel
import com.app.testkotlin.ui.home.HomeViewModel
import com.app.testkotlin.ui.login.LoginViewModel
import com.app.testkotlin.ui.profile.ProfileViewModel
import com.app.testkotlin.ui.register.RegisterViewModel
import com.app.testkotlin.ui.tracking.TrackingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CategoryViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { FoodViewModel(get()) }
    viewModel { FoodDetailViewModel(get()) }
    viewModel { TrackingViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { RegisterViewModel(get()) }

}

