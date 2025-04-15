package com.nhom6.weatherapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteLocation(
    @SerializedName("osm_id") val osmId: String,
    @SerializedName("osm_type") val osmType: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    @SerializedName("display_name") val displayName: String,
    val city: String,
    val country: String,
)