package com.nhom6.weatherapp.data.repository

import android.Manifest
import android.location.Geocoder
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.nhom6.weatherapp.data.CurrentLocation
import com.nhom6.weatherapp.data.remote.api.LocationAPI
import com.nhom6.weatherapp.data.remote.model.RemoteLocation
import android.util.Log

class LocationRepository(private val locationAPI: LocationAPI) {

    // Phương thức này yêu cầu quyền truy cập vị trí người dùng
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        onSuccess: (CurrentLocation) -> Unit,
        onFailure: () -> Unit
    ) {
        // Lấy vị trí người dùng hiện tại với độ chính xác cao
        fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, // Chỉ định độ chính xác cao cho vị trí
            CancellationTokenSource().token // Tạo token để hủy tác vụ nếu cần
        ).addOnSuccessListener { location ->
            if (location == null) {
                Log.e("LocationRepository", "Location is null")
                onFailure()
                return@addOnSuccessListener
            }
            onSuccess(
                CurrentLocation(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
        }.addOnFailureListener { exception ->
            Log.e("LocationRepository", "Failed to get location: ${exception.localizedMessage}")
            onFailure()
        }
    }

    @Suppress("DEPRECATION")
    fun updateAddressText(
        currentLocation: CurrentLocation,
        geocoder: Geocoder
    ) : CurrentLocation {
        val latitude = currentLocation.latitude ?: return currentLocation
        val longitude = currentLocation.longitude ?: return currentLocation
        return geocoder.getFromLocation(latitude, longitude, 1)?.let { addresses ->
            val address = addresses[0]
            val addressText = StringBuilder()
//            addressText.append(address.locality).append(", ")
//            addressText.append(address.adminArea).append(", ")
            addressText.append(address.subAdminArea)
            currentLocation.copy(
                location = addressText.toString(),
            )
        } ?: currentLocation
    }

    // Phương thức tìm kiếm vị trí từ chuỗi query
    suspend fun searchLocation(query: String): List<RemoteLocation>? {
        val response = locationAPI.searchLocation(query = query)
        return if (response.isSuccessful) response.body() else null
    }
}
