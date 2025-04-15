package com.nhom6.weatherapp.data.di

import com.doquocviet.weatherapp.storage.SharedPreferencesManager
import org.koin.dsl.module

val storageModule = module {
    single { SharedPreferencesManager(get(), get()) }
}