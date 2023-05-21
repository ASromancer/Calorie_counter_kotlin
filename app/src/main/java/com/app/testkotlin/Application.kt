package com.app.testkotlin

import android.app.Application
import com.app.testkotlin.di.apiModule
import com.app.testkotlin.di.repositoryModule
import com.app.testkotlin.di.retrofitModule
import com.app.testkotlin.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@MainApplication)
            modules(listOf(
                viewModelModule,
                apiModule,
                repositoryModule,
                retrofitModule
            ))
        }
    }
}