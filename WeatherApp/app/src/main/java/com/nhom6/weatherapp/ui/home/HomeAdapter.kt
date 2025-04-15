package com.nhom6.weatherapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nhom6.weatherapp.data.CurrentLocation
import com.nhom6.weatherapp.data.CurrentWeather
import com.nhom6.weatherapp.data.DailyForecast
import com.nhom6.weatherapp.data.HourlyForecast
import com.nhom6.weatherapp.data.WeatherData
import com.nhom6.weatherapp.databinding.ItemCurrentLocationBinding
import com.nhom6.weatherapp.databinding.ItemCurrentWeatherBinding
import com.nhom6.weatherapp.databinding.ItemForecastBinding

class HomeAdapter (
    private val onItemAction: (ActionType) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ActionType {
        SEARCH, SAVED_LOCATION, SETTING
    }

    private companion object {
        const val INDEX_CURRENT_LOCATION = 0
        const val INDEX_CURRENT_WEATHER = 1
        const val INDEX_HOURLY_FORECAST = 2
        const val INDEX_DAILY_FORECAST = 3
    }

    private val weatherData = mutableListOf<WeatherData>()

    fun setCurrentLocation(currentLocation: CurrentLocation) {
        if (weatherData.getOrNull(INDEX_CURRENT_LOCATION) != null) {
            weatherData[INDEX_CURRENT_LOCATION] = currentLocation
            notifyItemChanged(INDEX_CURRENT_LOCATION)
        } else {
            weatherData.add(INDEX_CURRENT_LOCATION, currentLocation)
            notifyItemInserted(INDEX_CURRENT_LOCATION)
        }
    }

    fun setCurrentWeather(currentWeather: CurrentWeather) {
        if (weatherData.getOrNull(INDEX_CURRENT_WEATHER) != null) {
            weatherData[INDEX_CURRENT_WEATHER] = currentWeather
            notifyItemChanged(INDEX_CURRENT_WEATHER)
        } else {
            weatherData.add(INDEX_CURRENT_WEATHER, currentWeather)
            notifyItemInserted(INDEX_CURRENT_WEATHER)
        }
    }

    fun setForecastData(
        hourlyForecasts: List<HourlyForecast>,
        dailyForecasts: List<DailyForecast>
    ) {
        // Xóa các item Forecast cũ
        weatherData.removeAll { it is HourlyForecast || it is DailyForecast }
        notifyDataSetChanged()

        // Thêm HourlyForecast trước
        val insertStartIndex = weatherData.size
        weatherData.addAll(hourlyForecasts)
        notifyItemRangeInserted(insertStartIndex, hourlyForecasts.size)

        // Sau đó thêm DailyForecast
        val dailyInsertStartIndex = weatherData.size
        weatherData.addAll(dailyForecasts)
        notifyItemRangeInserted(dailyInsertStartIndex, dailyForecasts.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            INDEX_CURRENT_LOCATION -> CurrentLocationViewHolder(
                ItemCurrentLocationBinding.inflate(LayoutInflater.from(parent.context),parent, false)
            )

            INDEX_CURRENT_WEATHER -> CurrentWeatherViewHolder(
                ItemCurrentWeatherBinding.inflate(LayoutInflater.from(parent.context),parent, false)
            )

            INDEX_HOURLY_FORECAST -> HourlyForecastViewHolder(
                ItemForecastBinding.inflate(LayoutInflater.from(parent.context),parent, false)
            )

            else -> DailyForecastViewHolder(
                ItemForecastBinding.inflate(LayoutInflater.from(parent.context),parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is CurrentLocationViewHolder -> {
                val currentLocation = weatherData[position] as CurrentLocation
                holder.bind(currentLocation)
            }
            is CurrentWeatherViewHolder -> {
                val currentWeather = weatherData[position] as CurrentWeather
                holder.bind(currentWeather)
            }
            is HourlyForecastViewHolder -> {
                val forecast = weatherData[position] as HourlyForecast
                holder.bind(forecast)
            }
            is DailyForecastViewHolder -> {
                val forecast = weatherData[position] as DailyForecast
                holder.bind(forecast)
            }
        }
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (weatherData[position]) {
            is CurrentLocation -> INDEX_CURRENT_LOCATION
            is CurrentWeather -> INDEX_CURRENT_WEATHER
            is HourlyForecast -> INDEX_HOURLY_FORECAST
            is DailyForecast -> INDEX_DAILY_FORECAST
        }
    }

    inner class CurrentLocationViewHolder(
        private val binding: ItemCurrentLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentLocation: CurrentLocation) {
            with(binding) {
                textCurrentLocation.text = currentLocation.location
                btnSetting.setOnClickListener { onItemAction(ActionType.SETTING) }
                btnLocationList.setOnClickListener { onItemAction(ActionType.SAVED_LOCATION) }
                btnSearch.setOnClickListener { onItemAction(ActionType.SEARCH) }
            }
        }
    }

    inner class CurrentWeatherViewHolder(
        private val binding: ItemCurrentWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentWeather: CurrentWeather) {
            with(binding) {
                // Cập nhật trường icon từ conditionIcon
                imageWeather.load("https:${currentWeather.conditionIcon}") {
                    crossfade(enable = true)
                }

                // Cập nhật hiển thị nhiệt độ từ temp
                textCurrentTemp.text = String.format("%s\u00B0C", currentWeather.temp)

                // Cập nhật các trường khác nếu cần, ví dụ:
                textFeelsLikeTemp.text = String.format("Feels like: %s\u00B0C", currentWeather.feelsLike)
//                textHumidity.text = "Humidity: ${currentWeather.humidity}%"
//                textWind.text = "Wind: ${currentWeather.wind} km/h"
//                textChanceOfRain.text = "Chance of rain: ${currentWeather.chanceOfRain}%"
                textCondition.text = currentWeather.conditionText
            }
        }
    }


    inner class DailyForecastViewHolder(
        private val binding: ItemForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: DailyForecast) {
//            with(binding) {
//                textTime.text = forecast.day
//                textTemperature.text = String.format("%s°C - %s°C", forecast.minTemp, forecast.maxTemp)
//                textFeelsLikeTemperature.text = "${forecast.chanceOfRain}%"
//                imageIcon.load(forecast.conditionIcon) {
//                    crossfade(true)
//                }
//            }
        }
    }

    inner class HourlyForecastViewHolder(
        private val binding: ItemForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: HourlyForecast) {
//            with(binding) {
//                textTime.text = forecast.time
//                textTemperature.text = String.format("%s°C", forecast.temp)
//                textFeelsLikeTemperature.text = ""
//                imageIcon.load(forecast.conditionIcon) {
//                    crossfade(true)
//                }
//            }
        }
    }
}