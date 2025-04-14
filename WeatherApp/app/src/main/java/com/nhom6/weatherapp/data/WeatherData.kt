package com.nhom6.weatherapp.data

import java.text.SimpleDateFormat
import java.util.Locale

sealed class WeatherData

data class CurrentLocation(
    val date: String = getCurrentDate(),
    val location: String = "Choose your location",
    val latitude: Double? = null,
    val longitude: Double? = null,
) : WeatherData()

data class CurrentWeather(
    val icon: String,
    val temperature: Float,
    val wind: Float,
    val humidity: Int,
    val chanceOfRain: Int
) : WeatherData()

data class Forecast(
    val time: String,
    val temperature: Float,
    val feelsLikeTemperature: Float,
    val icon: String
) : WeatherData()

private fun getCurrentDate(): String {
    val currentDate = System.currentTimeMillis()
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return "Today, ${formatter.format(currentDate)}"
}