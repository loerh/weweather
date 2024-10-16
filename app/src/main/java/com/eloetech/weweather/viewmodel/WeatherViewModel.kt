package com.eloetech.weweather.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eloetech.weweather.model.Forecast
import com.eloetech.weweather.model.GeocodeResponsePlace
import com.eloetech.weweather.model.WeatherResponseCurrent
import com.eloetech.weweather.model.db.LocationEntity
import com.eloetech.weweather.repository.GeocodeRepository
import com.eloetech.weweather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val geocodeRepository: GeocodeRepository
) : ViewModel() {

//    var currentWeather by mutableStateOf<WeatherResponse?>(null)
//        private set

    var current by mutableStateOf<Forecast?>(null)
    var forecast by mutableStateOf<List<Forecast>>(emptyList())
        private set
    var favoriteLocations by mutableStateOf<List<LocationEntity>>(emptyList())
        private set

    fun loadWeatherFirstMatch(address: String) {
        viewModelScope.launch {
//            val place = searchPlace(address)
//            val responseCurrent = getWeather(place.lat, place.lon)

            val geoResponse = geocodeRepository.getCoordinates(address)
            val first = geoResponse.first()
            val response = weatherRepository.getForecast(first.lat, first.lon)
            val responseCurrent = response.current

            current = Forecast(
                date = responseCurrent.time,
                temperature = responseCurrent.temperature_2m,
                condition = "$responseCurrent.weather_code",
                windSpeed = responseCurrent.wind_speed_10m.toInt(),
                humidity = responseCurrent.relative_humidity_2m.toDouble()
            )
        }
    }

    private suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponseCurrent {

//        viewModelScope.launch {
//            currentWeather = repository.getCurrentWeather(location)
//            forecast = repository.getWeatherForecast(location)

            val response = weatherRepository.getForecast(latitude, longitude)
        return response.current
//            current = Forecast(
//                date = response.current.time,
//                temperature = response.current.temperature_2m,
//                condition = "$response.current.weather_code",
//                windSpeed = response.current.wind_speed_10m.toInt(),
//                humidity = response.current.relative_humidity_2m.toDouble()
//            )
//        }
    }

    private suspend fun searchPlace(name: String): GeocodeResponsePlace {
        val response = geocodeRepository.getCoordinates(name)
        val firstMatch = response.first()
        return firstMatch
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            favoriteLocations = weatherRepository.getFavoriteLocations()
        }
    }

    fun addToFavorites(location: LocationEntity) {
        viewModelScope.launch {
            weatherRepository.addLocationToFavorites(location)
            loadFavorites()
        }
    }

    fun removeFromFavorites(location: LocationEntity) {
        viewModelScope.launch {
            weatherRepository.removeLocationFromFavorites(location)
            loadFavorites()
        }
    }
}