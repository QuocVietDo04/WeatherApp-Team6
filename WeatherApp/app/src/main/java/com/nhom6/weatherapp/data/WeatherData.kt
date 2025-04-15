package com.nhom6.weatherapp.data

import java.text.SimpleDateFormat
import java.util.Locale

sealed class WeatherData

data class CurrentLocation(
    val osmId: String? = "",
    val location: String = "Choose your location",
    val latitude: Double? = null,
    val longitude: Double? = null,
) : WeatherData()

data class CurrentWeather(
    val temp: Float,
    val feelsLike: Float,
    val wind: Float,
    val humidity: Int,
    val chanceOfRain: Int,
    val conditionIcon: String,
    val conditionText: String,
) : WeatherData()

data class DailyForecast(
    val day: String,
    val maxTemp: Float,
    val minTemp: Float,
    val chanceOfRain: Int,
    val conditionIcon: String,
) : WeatherData()

data class HourlyForecast(
    val time: String,
    val temp: Float,
    val conditionIcon: String,
) : WeatherData()

private fun getCurrentDate(): String {
    val currentDate = System.currentTimeMillis()
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return "Today, ${formatter.format(currentDate)}"
}