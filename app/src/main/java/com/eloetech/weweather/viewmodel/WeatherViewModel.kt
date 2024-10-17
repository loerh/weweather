package com.eloetech.weweather.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eloetech.weweather.model.DailyForecast
import com.eloetech.weweather.model.Forecast
import com.eloetech.weweather.model.GeocodeResponsePlace
import com.eloetech.weweather.model.Location
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

    var currentLocation by mutableStateOf<Location?>(null)
        private set
    var favoriteLocations by mutableStateOf<List<LocationEntity>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            loadFavorites()
        }
    }

    fun loadWeatherFirstMatch(address: String) {
        isLoading = true
        viewModelScope.launch {
            val geoResponse = geocodeRepository.getCoordinates(address)
            val first = geoResponse.first()
            val weatherResponse = weatherRepository.getForecast(first.lat, first.lon)
            val responseCurrent = weatherResponse.current

            val currentForecast = Forecast(
                date = responseCurrent.time,
                temperature = responseCurrent.temperature_2m,
                condition = "$responseCurrent.weather_code",
                windSpeed = responseCurrent.wind_speed_10m.toInt(),
                humidity = responseCurrent.relative_humidity_2m.toDouble()
            )

            val dailyForecasts = emptyList<DailyForecast>().toMutableList()

            weatherResponse.daily.time.forEachIndexed { index, element ->
                val dailyForecast = DailyForecast(
                    time = element,
                    temperatureMax = weatherResponse.daily.temperature_2m_max[index],
                    temperatureMin = weatherResponse.daily.temperature_2m_min[index],
                    sunrise = weatherResponse.daily.sunrise[index],
                    sunset = weatherResponse.daily.sunset[index],
                    precipitation = weatherResponse.daily.precipitation_sum[index],
                    precipitationProbability = weatherResponse.daily.precipitation_probability_max[index]
                )
                dailyForecasts += dailyForecast
            }

            currentLocation = Location(
                name = first.display_name,
                latitude = first.lat,
                longitude = first.lon,
                current = currentForecast,
                daily = dailyForecasts
            )

            isLoading = false
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

    fun onFavoriteSelection(location: Location) {
        viewModelScope.launch {
            var favorite: LocationEntity? = null
            for (favoriteLocation in favoriteLocations) {
                if (favoriteLocation.locationName == location.name) {
                    favorite = favoriteLocation
                    break
                }
            }

            if (favorite != null) {
                removeFromFavorites(favorite)
            } else {
                addToFavorites(LocationEntity(location.name, location.latitude, location.longitude))
            }
        }
    }

    fun isFavorite(locationName: String) : Boolean {
        return favoriteLocations
            .map { locationEntity -> locationEntity.locationName }
            .contains(locationName)
    }

    private fun addToFavorites(location: LocationEntity) {
        viewModelScope.launch {
            weatherRepository.addLocationToFavorites(location)
            loadFavorites()
        }
    }

    private fun removeFromFavorites(location: LocationEntity) {
        viewModelScope.launch {
            weatherRepository.removeLocationFromFavorites(location)
            loadFavorites()
        }
    }
}