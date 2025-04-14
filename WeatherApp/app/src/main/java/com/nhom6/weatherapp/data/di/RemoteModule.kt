package com.nhom6.weatherapp.data.di

import com.nhom6.weatherapp.data.remote.api.LocationAPI
import com.nhom6.weatherapp.data.remote.api.WeatherAPI
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val remoteModule = module {
    factory { okHttpClient() }          // Mỗi lần cần sẽ tạo mới một OkHttpClient
    single { retrofitForWeather(get()) }          // Chỉ tạo duy nhất một Retrofit dùng chung toàn app
    single { retrofitForLocation(get()) }          // Chỉ tạo duy nhất một Retrofit dùng chung toàn app
    factory { weatherAPI(get()) }       // Mỗi lần cần sẽ tạo mới một WeatherAPI, sử dụng Retrofit
    factory { locationAPI(get()) }      // Mỗi lần cần sẽ tạo mới một LocationAPI
}

// Người giao hàng – OkHttpClient chịu trách nhiệm kết nối và truyền tải yêu cầu HTTP
private fun okHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)   // Thời gian tối đa để kết nối tới server
        .writeTimeout(30, TimeUnit.SECONDS)     // Thời gian tối đa để gửi yêu cầu
        .readTimeout(30, TimeUnit.SECONDS)      // Thời gian tối đa để nhận phản hồi
        .retryOnConnectionFailure(false)        // Không thử lại khi kết nối thất bại
        .build()
}

// Công ty giao hàng – Retrofit kết hợp OkHttpClient để gửi và nhận yêu cầu từ server
private fun retrofitForWeather(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)                            // Chỉ định người giao hàng (OkHttpClient)
        .baseUrl(WeatherAPI.BASE_URL)                    // Địa chỉ API (nơi gửi yêu cầu)
        .addConverterFactory(GsonConverterFactory.create()) // Chuyển đổi dữ liệu JSON sang Kotlin
        .build()
}

private fun retrofitForLocation(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)                            // Chỉ định người giao hàng (OkHttpClient)
        .baseUrl(LocationAPI.BASE_URL)                    // Địa chỉ API (nơi gửi yêu cầu)
        .addConverterFactory(GsonConverterFactory.create()) // Chuyển đổi dữ liệu JSON sang Kotlin
        .build()
}

// Danh bạ giao hàng – Chỉ định các yêu cầu cụ thể (API) mà chúng ta sẽ gửi
private fun weatherAPI(retrofit: Retrofit): WeatherAPI {
    return retrofit.create(WeatherAPI::class.java)  // Tạo đối tượng API từ Retrofit để gửi yêu cầu
}

private fun locationAPI(retrofit: Retrofit): LocationAPI {
    return retrofit.create(LocationAPI::class.java)  // Tạo đối tượng API từ Retrofit để gửi yêu cầu
}