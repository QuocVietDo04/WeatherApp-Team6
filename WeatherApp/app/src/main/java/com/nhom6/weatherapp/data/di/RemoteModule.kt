package com.nhom6.weatherapp.data.di

import com.nhom6.weatherapp.data.remote.api.LocationAPI
import com.nhom6.weatherapp.data.remote.api.WeatherAPI
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val remoteModule = module {
    // Tạo OkHttpClient mới mỗi lần
    factory { okHttpClient() }

    // Tạo hai Retrofit khác nhau, phân biệt bằng tên
    single(named("weather")) { retrofitForWeather(get()) }
    single(named("location")) { retrofitForLocation(get()) }

    // Tạo API từ Retrofit tương ứng
    factory { weatherAPI(get(named("weather"))) }
    factory { locationAPI(get(named("location"))) }
}

// OkHttpClient dùng chung
private fun okHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build()
}

// Retrofit cho Weather
private fun retrofitForWeather(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(WeatherAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

// Retrofit cho Location
private fun retrofitForLocation(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(LocationAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

// Tạo WeatherAPI từ Retrofit weather
private fun weatherAPI(retrofit: Retrofit): WeatherAPI {
    return retrofit.create(WeatherAPI::class.java)
}

// Tạo LocationAPI từ Retrofit location
private fun locationAPI(retrofit: Retrofit): LocationAPI {
    return retrofit.create(LocationAPI::class.java)
}
