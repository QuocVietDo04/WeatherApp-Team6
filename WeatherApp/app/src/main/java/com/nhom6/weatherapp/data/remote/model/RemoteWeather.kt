package com.nhom6.weatherapp.data.remote.model

import com.google.gson.annotations.SerializedName
import com.nhom6.weatherapp.data.CurrentWeather
import com.nhom6.weatherapp.data.DailyForecast
import com.nhom6.weatherapp.data.HourlyForecast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class RemoteWeather(
    val current: CurrentWeatherDto,
    val forecast: ForecastWeatherDto
)

data class CurrentWeatherDto(
    val last_updated: String,
    @SerializedName("temp_c") val tempC: Float,
    @SerializedName("temp_f") val tempF: Float,
    @SerializedName("feelslike_c") val feelsLikeC: Float,
    @SerializedName("feelslike_f") val feelsLikeF: Float,
    @SerializedName("wind_mph") val windMph: Float,
    @SerializedName("wind_kph") val windKph: Float,
    val humidity: Int,
    val uv: Double,
    val condition: WeatherConditionDto,
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

fun mapRemoteWeatherToCurrentWeather(remote: RemoteWeather): CurrentWeather {
    val current = remote.current
    val forecastToday = remote.forecast.forecastDay.firstOrNull()?.day

    return CurrentWeather(
        temp = current.tempC,
        feelsLike = current.feelsLikeC,
        wind = current.windMph,
        humidity = current.humidity,
        chanceOfRain = forecastToday?.chanceOfRain ?: 0, // fallback nếu null
        conditionIcon = current.condition.icon,
        conditionText = current.condition.text,
    )
}

fun mapRemoteWeatherToDailyForecast(remote: RemoteWeather): List<DailyForecast> {
    return remote.forecast.forecastDay.map { forecastDayDto ->
        DailyForecast(
            day = getDayOfWeek(forecastDayDto.date),
            maxTemp = forecastDayDto.day.maxTempC,
            minTemp = forecastDayDto.day.minTempC,
            chanceOfRain = forecastDayDto.day.chanceOfRain,
            conditionIcon = "https:${forecastDayDto.day.condition.icon}"
        )
    }
}

fun mapRemoteWeatherToHourlyForecast(remote: RemoteWeather): List<HourlyForecast> {
    val todayHours = remote.forecast.forecastDay.firstOrNull()?.hour ?: emptyList()

    return todayHours.map { hourDto ->
        HourlyForecast(
            time = hourDto.time.substringAfter(" "), // lấy phần giờ
            temp = hourDto.tempC,
            conditionIcon = "https:${hourDto.condition.icon}"
        )
    }
}

fun getDayOfWeek(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val targetDate = inputFormat.parse(dateString) ?: return ""

        val today = Calendar.getInstance()
        val inputDate = Calendar.getInstance().apply { time = targetDate }

        val isToday = today.get(Calendar.YEAR) == inputDate.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == inputDate.get(Calendar.DAY_OF_YEAR)

        if (isToday) {
            "Today"
        } else {
            val outputFormat = SimpleDateFormat("EEE", Locale.ENGLISH) // "EEE" = viết tắt thứ
            outputFormat.format(targetDate)
        }
    } catch (e: Exception) {
        ""
    }
}

