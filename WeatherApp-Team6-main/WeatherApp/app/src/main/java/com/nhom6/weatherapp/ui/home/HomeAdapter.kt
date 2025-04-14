package com.nhom6.weatherapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nhom6.weatherapp.data.CurrentLocation
import com.nhom6.weatherapp.data.CurrentWeather
import com.nhom6.weatherapp.data.Forecast
import com.nhom6.weatherapp.data.WeatherData
import com.nhom6.weatherapp.databinding.ItemCurrentLocationBinding
import com.nhom6.weatherapp.databinding.ItemCurrentWeatherBinding
import com.nhom6.weatherapp.databinding.ItemForecastBinding

class HomeAdapter (
    private val onLocationClicked: () -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private companion object {
        const val INDEX_CURRENT_LOCATION = 0
        const val INDEX_CURRENT_WEATHER = 1
        const val INDEX_FORECAST = 2
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

    fun setForecastData(forecast: List<Forecast>) {
        weatherData.removeAll { it is Forecast }
        notifyItemRangeRemoved(INDEX_FORECAST, weatherData.size)
        weatherData.addAll(INDEX_FORECAST, forecast)
        notifyItemRangeInserted(INDEX_FORECAST, forecast.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            INDEX_CURRENT_LOCATION -> CurrentLocationViewHolder(
                ItemCurrentLocationBinding.inflate(LayoutInflater.from(parent.context),parent, false)
            )

            INDEX_CURRENT_WEATHER -> CurrentWeatherViewHolder(
                ItemCurrentWeatherBinding.inflate(LayoutInflater.from(parent.context),parent, false)
            )

            else -> ForecastViewHolder(
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
            is ForecastViewHolder -> {
                val forecast = weatherData[position] as Forecast
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
            is Forecast -> INDEX_FORECAST
        }
    }

    inner class CurrentLocationViewHolder(
        private val binding: ItemCurrentLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentLocation: CurrentLocation) {
            with(binding) {
                textCurrentLocation.text = currentLocation.location
                btnSetting.setOnClickListener { onLocationClicked() }
                btnLocationList.setOnClickListener { onLocationClicked() }
                btnSearch.setOnClickListener { onLocationClicked() }
            }
        }
    }

    inner class CurrentWeatherViewHolder(
        private val binding: ItemCurrentWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentWeather: CurrentWeather) {
            with(binding) {
                imageWeather.load("https:${currentWeather.icon}") {
                    crossfade(enable = true)
                }
                textCurrentTemp.text = String.format("%s\u00B0C", currentWeather.temperature)
            }
        }
    }

    inner class ForecastViewHolder(
        private val binding: ItemForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(forecast: Forecast) {
            with(binding) {
//                textTime.text = forecast.time
//                textTemperature.text = String.format("%s\u00B0C", forecast.temperature)
//                textFeelsLikeTemperature.text = String.format("%s\u00B0C", forecast.feelsLikeTemperature)
//                imageIcon.load("https:${forecast.icon}") {
//                    crossfade(enable = true)
//                }
            }
        }
    }
}