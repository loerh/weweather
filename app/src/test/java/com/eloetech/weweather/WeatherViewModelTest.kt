package com.eloetech.weweather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eloetech.weweather.model.db.LocationEntity
import com.eloetech.weweather.repository.GeocodeRepository
import com.eloetech.weweather.repository.WeatherRepository
import com.eloetech.weweather.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    // Rule to make LiveData work synchronously in tests
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Provides a TestCoroutineDispatcher and TestCoroutineScope for testing coroutines
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var geocodeRepository: GeocodeRepository

    @Before
    fun setUp() {
        // Initialize mocks
        weatherRepository = mock(WeatherRepository::class.java)
        geocodeRepository = mock(GeocodeRepository::class.java)
        weatherViewModel = WeatherViewModel(
            weatherRepository,
            geocodeRepository
        )

        // Set the test dispatcher as the default one
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher after tests
    }

    @Test
    fun `test loadFavoriteLocations updates state with favorites`() = runBlocking {
        // Arrange
        val favoriteLocations = listOf(
            LocationEntity(
                locationName = "New York",
                latitude = 1.0,
                longitude = -2.0
            ),
            LocationEntity(
                locationName = "London",
                latitude = 3.233,
                longitude = 0.113
            )
        )

        // When repository is called, return the mock favorite locations
        whenever(weatherRepository.getFavoriteLocations()).thenReturn(favoriteLocations)

        // Act
        weatherViewModel.loadFavorites()
        testDispatcher.scheduler.advanceUntilIdle() // Make sure all coroutines finish

        // Assert
        assert(weatherViewModel.favoriteLocations == favoriteLocations)
    }

    @Test
    fun `test addLocationToFavorites calls repository and updates state`() = runBlocking {
        // Arrange
        val location = LocationEntity(
            locationName = "Paris",
            latitude = 1.553,
            longitude = 1.43
        )
        val favoriteLocations = listOf(location)

        // Set up the repository to return an updated list
        whenever(weatherRepository.getFavoriteLocations()).thenReturn(favoriteLocations)

        // Act
        weatherViewModel.addToFavorites(location)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert that repository's addLocationToFavorites method is called
        verify(weatherRepository).addLocationToFavorites(location)

        // Check that the favorite locations are updated in the ViewModel
        assert(weatherViewModel.favoriteLocations == favoriteLocations)
    }

    @Test
    fun `test removeLocationFromFavorites calls repository and updates state`() = runBlocking {
        // Arrange
        val location = LocationEntity(
            locationName = "New York",
            latitude = 1.0,
            longitude = -2.0
        )
        val favoriteLocations = emptyList<LocationEntity>()

        // Set up the repository to return an empty list after removal
        whenever(weatherRepository.getFavoriteLocations()).thenReturn(favoriteLocations)

        // Act
        weatherViewModel.removeFromFavorites(location)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert that repository's removeLocationFromFavorites method is called
        verify(weatherRepository).removeLocationFromFavorites(location)

        // Check that the favorite locations are updated in the ViewModel
        assert(weatherViewModel.favoriteLocations == favoriteLocations)
    }
}