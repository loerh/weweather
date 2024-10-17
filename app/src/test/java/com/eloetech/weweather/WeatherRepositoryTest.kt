package com.eloetech.weweather

import com.eloetech.weweather.api.WeatherApi
import com.eloetech.weweather.model.db.LocationEntity
import com.eloetech.weweather.model.db.WeatherDao
import com.eloetech.weweather.repository.WeatherRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*


class WeatherRepositoryTest {

    private lateinit var weatherRepository: WeatherRepository
    private lateinit var weatherDao: WeatherDao
    private lateinit var weatherApi: WeatherApi

    @Before
    fun setUp() {
        weatherDao = mock(WeatherDao::class.java)
        weatherApi = mock(WeatherApi::class.java)
        weatherRepository = WeatherRepository(weatherApi, weatherDao)
    }

    @Test
    fun `test getFavoriteLocations calls DAO`(): Unit = runBlocking {
        // Act
        weatherRepository.getFavoriteLocations()

        // Assert
        verify(weatherDao).getAllFavorites()
    }

    @Test
    fun `test addLocationToFavorites calls DAO`() = runBlocking {
        // Arrange
        val location = LocationEntity(
            locationName = "New York",
            latitude = 1.0,
            longitude = -2.0
        )

        // Act
        weatherRepository.addLocationToFavorites(location)

        // Assert
        verify(weatherDao).insert(location)
    }

    @Test
    fun `test removeLocationFromFavorites calls DAO`() = runBlocking {
        // Arrange
        val location = LocationEntity(
            locationName = "New York",
            latitude = 1.0,
            longitude = -2.0
        )

        // Act
        weatherRepository.removeLocationFromFavorites(location)

        // Assert
        verify(weatherDao).delete(location)
    }
}