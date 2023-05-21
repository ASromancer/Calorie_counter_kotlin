package com.app.testkotlin.di

import com.app.testkotlin.repository.CategoryRepository
import com.app.testkotlin.repository.FavoriteRepository
import com.app.testkotlin.repository.LoginRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { CategoryRepository(get()) }
    single { LoginRepository(get()) }
    single { FavoriteRepository(get()) }
}




