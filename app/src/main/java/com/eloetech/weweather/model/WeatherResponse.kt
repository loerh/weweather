package com.eloetech.weweather.model

data class WeatherResponse(
    val location: String,
    val temperature: Double,
    val windSpeed: Double,
    val humidity: Int,
    val forecast: List<Forecast>
)