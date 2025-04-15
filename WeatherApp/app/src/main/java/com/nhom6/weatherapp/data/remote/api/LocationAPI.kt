package com.nhom6.weatherapp.data.remote.api

import com.nhom6.weatherapp.BuildConfig
import com.nhom6.weatherapp.data.remote.model.RemoteLocation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationAPI {

    companion object {
        const val BASE_URL = BuildConfig.LOCATION_BASE_URL
    }

    @GET("search")
    suspend fun searchLocation(
        @Query("format") format: String = "jsonv2",
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("accept-language") language: String = "vi"
    ): Response<List<RemoteLocation>>

}