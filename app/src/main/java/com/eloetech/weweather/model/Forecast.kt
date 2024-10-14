package com.eloetech.weweather.model

data class Forecast(
    val date: String,
    val temperature: Double,
    val condition: String,
    val windSpeed: Int,
    val humidity: Double
)