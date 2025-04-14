package com.nhom6.weatherapp

import android.app.Application
import com.nhom6.weatherapp.data.di.remoteModule
import com.nhom6.weatherapp.data.di.repositoryModule
import com.nhom6.weatherapp.data.di.storageModule
import com.nhom6.weatherapp.data.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Android context
            androidContext(this@MyApplication)
            modules(
                listOf(
                    repositoryModule,
                    viewModelModule,
                    remoteModule,
                    storageModule,
                )
            )
        }
    }
}