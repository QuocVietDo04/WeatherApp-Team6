package com.nhom6.weatherapp.ui.home

import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.nhom6.weatherapp.data.CurrentLocation
import com.nhom6.weatherapp.data.CurrentWeather
import com.nhom6.weatherapp.data.DailyForecast
import com.nhom6.weatherapp.data.HourlyForecast
import com.nhom6.weatherapp.data.LiveDataEvent

import com.nhom6.weatherapp.data.remote.api.WeatherAPI
import com.nhom6.weatherapp.data.remote.model.RemoteWeather
import com.nhom6.weatherapp.data.remote.model.mapRemoteWeatherToCurrentWeather
import com.nhom6.weatherapp.data.remote.model.mapRemoteWeatherToDailyForecast
import com.nhom6.weatherapp.data.remote.model.mapRemoteWeatherToHourlyForecast

import com.nhom6.weatherapp.data.repository.LocationRepository
import com.nhom6.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _currentLocation = MutableLiveData<LiveDataEvent<CurrentLocationDataState>>()
    val currentLocation: LiveData<LiveDataEvent<CurrentLocationDataState>> get() = _currentLocation

    private fun emitCurrentLocationUiState(
        isLoading: Boolean = false,
        currentLocation: CurrentLocation? = null,
        error: String? = null
    ) {
        val currentLocationDataState = CurrentLocationDataState(isLoading, currentLocation, error)
        _currentLocation.value = LiveDataEvent(currentLocationDataState)
    }

    data class CurrentLocationDataState(
        val isLoading: Boolean,
        val currentLocation: CurrentLocation?,
        val error: String?
    )

    @Suppress("MissingPermission")
    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        geocoder: Geocoder
    ) {
        viewModelScope.launch {
            emitCurrentLocationUiState(isLoading = true)
            locationRepository.getCurrentLocation(
                fusedLocationProviderClient = fusedLocationProviderClient,
                onSuccess = { currentLocation ->
                    updateAddressText(currentLocation, geocoder)
                },
                onFailure = {
                    emitCurrentLocationUiState(error = "Unable to fetch current location")
                }
            )
        }
    }

    private fun updateAddressText(currentLocation: CurrentLocation, geocoder: Geocoder) {
        viewModelScope.launch {
            runCatching {
                locationRepository.updateAddressText(currentLocation, geocoder)
            }.onSuccess { location ->
                emitCurrentLocationUiState(currentLocation = location)
            }.onFailure {
                emitCurrentLocationUiState(
                    currentLocation = currentLocation.copy(
                        location = "N/A"
                    )
                )
            }
        }
    }
    //endregion

    //region_weather_data
    private val _weatherData = MutableLiveData<LiveDataEvent<WeatherDataState>>()
    val weatherData: LiveData<LiveDataEvent<WeatherDataState>> get() = _weatherData

    data class WeatherDataState(
        val isLoading: Boolean,
        val currentWeather: CurrentWeather?,
        val dailyForecast: List<DailyForecast>?,
        val hourlyForecast: List<HourlyForecast>?,
        val error: String?
    )

    private fun emitWeatherDataUiState(
        isLoading: Boolean = false,
        currentWeather: CurrentWeather? = null,
        dailyForecast: List<DailyForecast>? = null,
        hourlyForecast: List<HourlyForecast>? = null,
        error: String? = null
    ) {
        val weatherDataState = WeatherDataState(isLoading, currentWeather, dailyForecast, hourlyForecast, error)
        _weatherData.value = LiveDataEvent(weatherDataState)
    }

    fun getWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            emitWeatherDataUiState(isLoading = true)
            weatherRepository.getWeatherData(latitude, longitude)?.let { weatherData ->
                val currentWeather = mapRemoteWeatherToCurrentWeather(weatherData)
                val dailyForecast = mapRemoteWeatherToDailyForecast(weatherData)
                val hourlyForecast = mapRemoteWeatherToHourlyForecast(weatherData)

                emitWeatherDataUiState(
                    currentWeather = currentWeather,
                    dailyForecast = dailyForecast,
                    hourlyForecast = hourlyForecast
                )
            } ?: emitWeatherDataUiState(error = "Unable to fetch weather data")
        }
    }

    private fun getForecastTime(dateTime: String): String {
        val pattern = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = pattern.parse(dateTime) ?: return dateTime
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
    }
    //endregion
}