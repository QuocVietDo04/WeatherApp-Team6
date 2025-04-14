package com.nhom6.weatherapp.data.di

import com.nhom6.weatherapp.data.repository.LocationRepository
import com.nhom6.weatherapp.data.repository.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { LocationRepository(get()) }
    single { WeatherRepository(get()) }
}