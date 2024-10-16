package com.eloetech.weweather.repository

import com.eloetech.weweather.BuildConfig
import com.eloetech.weweather.api.GeocodeApi
import com.eloetech.weweather.model.GeocodeResponsePlace
import okio.IOException
import javax.inject.Inject

class GeocodeRepository @Inject constructor(
    private val geocodeApi: GeocodeApi
) {
    suspend fun getCoordinates(name: String): List<GeocodeResponsePlace> {
        return try {
            val foo = geocodeApi.getCoordinates(name, BuildConfig.GEOCODE_API_KEY)
            println(foo)
            geocodeApi.getCoordinates(name, BuildConfig.GEOCODE_API_KEY).body() ?: emptyList()
        } catch (e: IOException) {
            println(e.localizedMessage)
            return emptyList()
        }
    }
}