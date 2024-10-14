package com.eloetech.weweather.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eloetech.weweather.model.Forecast
import com.eloetech.weweather.model.WeatherResponse
import com.eloetech.weweather.model.db.LocationEntity
import com.eloetech.weweather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    var currentWeather by mutableStateOf<WeatherResponse?>(null)
        private set
    var forecast by mutableStateOf<List<Forecast>>(emptyList())
        private set
    var favoriteLocations by mutableStateOf<List<LocationEntity>>(emptyList())
        private set

    fun loadWeather(location: String) {
        viewModelScope.launch {
            currentWeather = repository.getCurrentWeather(location)
            forecast = repository.getWeatherForecast(location)
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            favoriteLocations = repository.getFavoriteLocations()
        }
    }

    fun addToFavorites(location: LocationEntity) {
        viewModelScope.launch {
            repository.addLocationToFavorites(location)
            loadFavorites()
        }
    }

    fun removeFromFavorites(location: LocationEntity) {
        viewModelScope.launch {
            repository.removeLocationFromFavorites(location)
            loadFavorites()
        }
    }
}