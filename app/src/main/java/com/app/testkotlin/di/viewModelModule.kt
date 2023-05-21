package com.app.testkotlin.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.testkotlin.model.Category
import com.app.testkotlin.repository.CategoryRepository
import com.app.testkotlin.ui.category.CategoryViewModel
import com.app.testkotlin.ui.favorite.FavoriteViewModel
import com.app.testkotlin.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val viewModelModule = module {
    viewModel { CategoryViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
}

