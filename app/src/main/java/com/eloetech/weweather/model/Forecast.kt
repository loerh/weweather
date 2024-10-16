package com.eloetech.weweather.model

data class Forecast(
    val date: String,
    val temperature: Double,
    val condition: String,
    val windSpeed: Int,
    val humidity: Double
)

data class DailyForecast(
    val time: String,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val sunrise: String,
    val sunset: String,
    val precipitation: String,
    val precipitationProbability: String
)