package com.nhom6.weatherapp.data.repository

import com.nhom6.weatherapp.data.remote.api.WeatherAPI
import com.nhom6.weatherapp.data.remote.model.RemoteWeather

class WeatherRepository(private val weatherAPI: WeatherAPI){
    suspend fun getWeatherData(latitude: Double, longitude: Double): RemoteWeather? {
        val response = weatherAPI.getWeatherData(query = "$latitude,$longitude")
        return if (response.isSuccessful) response.body() else null
    }
}