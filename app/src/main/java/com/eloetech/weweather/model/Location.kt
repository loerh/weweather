package com.eloetech.weweather.model

data class Location(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val current: Forecast,
    val daily: List<DailyForecast>
)