package com.eloetech.weweather.model

data class WeatherResponse(
    val current: WeatherResponseCurrent
)

data class WeatherResponseCurrent (
    val time: String,
    val interval: Int,
    val temperature_2m: Double,
    val relative_humidity_2m: Int,
    val weather_code: Int,
    val wind_speed_10m: Double
)