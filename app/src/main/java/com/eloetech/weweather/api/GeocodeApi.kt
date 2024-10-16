package com.eloetech.weweather.api

import com.eloetech.weweather.model.GeocodeResponsePlace
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodeApi {
    @GET("search")
    suspend fun getCoordinates(
        @Query("q") address: String,
        @Query("api_key") apiKey: String
    ): Response<List<GeocodeResponsePlace>>
}