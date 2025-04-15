package com.nhom6.weatherapp.data.di

import com.nhom6.weatherapp.ui.home.HomeViewModel
import com.nhom6.weatherapp.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
}