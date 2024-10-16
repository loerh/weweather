package com.eloetech.weweather.repository

import com.eloetech.weweather.BuildConfig
import com.eloetech.weweather.api.WeatherApi
import com.eloetech.weweather.model.Forecast
import com.eloetech.weweather.model.WeatherResponse
import com.eloetech.weweather.model.db.LocationEntity
import com.eloetech.weweather.model.db.WeatherDao
import okio.IOException
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao // Room DAO
) {
    suspend fun getForecast(latitude: Double, longitude: Double): WeatherResponse {
        return try {
            weatherApi.getForecast(latitude, longitude)
        } catch (e: IOException) {
            println(e.localizedMessage)
            return WeatherResponse(
                current = TODO(),
                daily = TODO()
            )
        }
    }

    // Favorite Locations methods using Room DB
    suspend fun getFavoriteLocations(): List<LocationEntity> {
        return weatherDao.getAllFavorites()
    }

    suspend fun addLocationToFavorites(locationEntity: LocationEntity) {
        weatherDao.insert(locationEntity)
    }

    suspend fun removeLocationFromFavorites(locationEntity: LocationEntity) {
        weatherDao.delete(locationEntity)
    }
}