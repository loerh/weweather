package com.eloetech.weweather.model

data class WeatherResponse(
    val current: WeatherResponseCurrent,
    val daily: WeatherResponseDaily
)

data class WeatherResponseCurrent (
    val time: String,
    val interval: Int,
    val temperature_2m: Double,
    val relative_humidity_2m: Int,
    val weather_code: Int,
    val wind_speed_10m: Double
)

data class WeatherResponseDaily (
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>,
    val precipitation_sum: List<String>,
    val precipitation_probability_max: List<String>
)