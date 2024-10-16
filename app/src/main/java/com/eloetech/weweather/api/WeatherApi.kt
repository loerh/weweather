package com.eloetech.weweather.api

import com.eloetech.weweather.model.Forecast
import com.eloetech.weweather.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m,cloud_cover",
        @Query("daily") daily: String = "weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_sum,precipitation_probability_max",
        @Query("forecast_days") forecastDays: Int = 10
    ): WeatherResponse
}