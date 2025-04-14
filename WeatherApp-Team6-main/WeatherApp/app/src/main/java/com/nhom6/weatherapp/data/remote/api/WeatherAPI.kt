package com.nhom6.weatherapp.data.remote.api

import com.nhom6.weatherapp.BuildConfig
import com.nhom6.weatherapp.data.remote.model.RemoteWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    companion object {
        const val BASE_URL = BuildConfig.WEATHER_BASE_URL
        const val API_KEY = BuildConfig.WEATHER_API_KEY
    }

    @GET("forecast.json")
    suspend fun getWeatherData(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String,
    ): Response<RemoteWeather>

}