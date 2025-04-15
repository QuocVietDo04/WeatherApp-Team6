package com.nhom6.weatherapp.data.di

import com.google.gson.Gson
import org.koin.dsl.module

val serializerModule = module {
    single { Gson() }
}