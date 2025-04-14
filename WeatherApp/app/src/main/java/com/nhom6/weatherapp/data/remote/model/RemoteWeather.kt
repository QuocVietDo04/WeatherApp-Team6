package com.nhom6.weatherapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteWeather(
    val location: RemoteLocation,
    val current: CurrentWeatherDto,
    val forecast: ForecastWeatherDto
)

data class CurrentWeatherDto(
    val last_updated: String,
    @SerializedName("temp_c") val tempC: Float,
    @SerializedName("temp_f") val tempF: Float,
    @SerializedName("is_day") val isDay: Int,
    val condition: WeatherConditionDto,
    @SerializedName("wind_kph") val wind: Float,
    val humidity: Int,
    @SerializedName("feelslike_c") val feelsLikeC: Float,
    @SerializedName("feelslike_f") val feelsLikeF: Float,
    val uv: Double
)

data class ForecastWeatherDto(
    @SerializedName("forecastday") val forecastDay: List<ForecastDayDto>
)

data class ForecastDayDto(
    val date: String,
    val day: DayDto,
    val hour: List<HourDto>
)

data class DayDto(
    @SerializedName("daily_chance_of_rain") val chanceOfRain: Int,
    @SerializedName("maxtemp_c") val maxTempC: Float,
    @SerializedName("maxtemp_f") val maxTempF: Float,
    @SerializedName("mintemp_c") val minTempC: Float,
    @SerializedName("mintemp_f") val minTempF: Float,
    val condition: WeatherConditionDto
)

data class HourDto(
    val time: String,
    @SerializedName("temp_c") val tempC: Float,
    @SerializedName("temp_f") val tempF: Float,
    val condition: WeatherConditionDto
)

data class WeatherConditionDto(
    val text: String,
    val icon: String,
    val code: Int
)
