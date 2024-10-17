package com.eloetech.weweather.model

import androidx.compose.ui.text.capitalize
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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
) {
    fun formattedDateString(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(time , formatter)
        return "${date.month.toString().capitalize(Locale.ROOT).take(3)} ${date.dayOfMonth}"
    }

    fun formattedSunrise(): String {
        return formattedTime(sunrise)
    }

    fun formattedSunset(): String {
        return formattedTime(sunset)
    }

    private fun formattedTime(time: String,
                              pattern: String = "yyyy-MM-dd'T'HH:mm"): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val localTime = LocalTime.parse(time, formatter)
        return "${localTime.hour}:${localTime.minute}"
    }

}