package com.doquocviet.weatherapp.storage

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.nhom6.weatherapp.data.CurrentLocation

class SharedPreferencesManager(context: Context, private val gson: Gson) {

    private companion object {
        const val PREF_NAME = "WeatherAppPref"
        const val KEY_CURRENT_LOCATION = "currentLocation"
    }

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveCurrentLocation(currentLocation: CurrentLocation) {
        val currentLocationJson = gson.toJson(currentLocation)
        sharedPreferences.edit {
            putString(KEY_CURRENT_LOCATION, currentLocationJson)
        }
    }

    fun getCurrentLocation(): CurrentLocation? {
        return sharedPreferences.getString(KEY_CURRENT_LOCATION, null)?.let { currentLocationJson ->
            gson.fromJson(currentLocationJson, CurrentLocation::class.java)
        }
    }

    fun saveLocationList(locationList: List<CurrentLocation>) {
        val json = gson.toJson(locationList)
        sharedPreferences.edit {
            putString("locationList", json)
        }
    }

    fun getLocationList(): List<CurrentLocation>? {
        val json = sharedPreferences.getString("locationList", null)
        return json?.let {
            val type = object : com.google.gson.reflect.TypeToken<List<CurrentLocation>>() {}.type
            gson.fromJson(it, type)
        }
    }

}