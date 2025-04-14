package com.nhom6.weatherapp.data.di

import com.nhom6.weatherapp.data.repository.LocationRepository
import com.nhom6.weatherapp.data.repository.WeatherRepository
import org.koin.dsl.module

/**
 * Module for providing repository instances.
 *
 * This module defines the dependencies for the repositories used in the application.
 * It uses Koin for dependency injection.
 */
val repositoryModule = module {
    single { LocationRepository(get()) }
    single { WeatherRepository(get()) }
}