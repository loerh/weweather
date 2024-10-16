package com.eloetech.weweather.model

data class Location(
    val name: String,
    val current: Forecast,
    val daily: List<DailyForecast>
)