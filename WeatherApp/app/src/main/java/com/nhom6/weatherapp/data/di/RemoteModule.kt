package com.nhom6.weatherapp.data.di

import android.util.Log
import com.nhom6.weatherapp.data.remote.api.LocationAPI
import com.nhom6.weatherapp.data.remote.api.WeatherAPI
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Định nghĩa các qualifiers cho OkHttpClient và Retrofit
val weatherQualifier = named("WeatherOkHttpClient")
val locationQualifier = named("LocationOkHttpClient")
val weatherRetrofitQualifier = named("WeatherRetrofit")
val locationRetrofitQualifier = named("LocationRetrofit")

val remoteModule = module {
    // Inject OkHttpClient riêng cho từng API
    factory(weatherQualifier) { weatherOkHttpClient() }
    factory(locationQualifier) { locationOkHttpClient() }

    // Inject Retrofit với OkHttpClient tương ứng
    single(weatherRetrofitQualifier) { retrofitForWeather(get(weatherQualifier)) }
    single(locationRetrofitQualifier) { retrofitForLocation(get(locationQualifier)) }

    // Inject các API với Retrofit tương ứng
    factory { weatherAPI(get(weatherRetrofitQualifier)) }
    factory { locationAPI(get(locationRetrofitQualifier)) }
}

// Người giao hàng – OkHttpClient chịu trách nhiệm kết nối và truyền tải yêu cầu HTTP
private fun weatherOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)

        .build()
}

private fun locationOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            // Log the response URL after request
            Log.d("OkHttp", "Request URL: ${request.url}")
            Log.d("OkHttp", "Response URL: ${response.request.url}")

            response
        }
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