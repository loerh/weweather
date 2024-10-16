package com.eloetech.weweather.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.eloetech.weweather.model.Forecast
import com.eloetech.weweather.model.db.LocationEntity
import com.eloetech.weweather.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weather = viewModel.current
    val forecast = viewModel.forecast
    val favoriteLocations = viewModel.favoriteLocations

    Column {

        SearchComponent { address -> viewModel.loadWeatherFirstMatch(address) }
        weather?.let {
            WeatherDetails(weather)
        }

        FavoriteLocations(favoriteLocations, viewModel)

        LazyColumn {
            items(forecast) { day ->
                ForecastItem(day)
            }
        }
    }
}

@Composable
fun SearchComponent(onSearch: (String) -> Unit) {

    var address by remember { mutableStateOf("") }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeContent),
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        value = address,
        onValueChange = { address = it },
        label = { Text("Search Place") },
        trailingIcon = {
            IconButton(onClick = { onSearch(address) }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}

@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeContent),
        value = text,
        onValueChange = { text = it },
        label = { Text("Search for a city") },
        trailingIcon = {
            IconButton(onClick = { onSearch(text) }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}

@Composable
fun WeatherDetails(weather: Forecast) {
    Column {
//        Text(text = "Location: ${weather.location}")
        Text(text = "Temperature: ${weather.temperature}°C")
        Text(text = "Wind Speed: ${weather.windSpeed} m/s")
        Text(text = "Humidity: ${weather.humidity}%")
    }
}

@Composable
fun FavoriteLocations(
    favoriteLocations: List<LocationEntity>, // List of favorite locations
    onLocationSelected: WeatherViewModel // Callback when a location is tapped
) {
    LazyColumn {
        items(favoriteLocations) { location ->
            LocationItem(
                location = location,
                onLocationSelected = {
                    println("Location Selected: $location")
//                    onLocationSelected.loadWeather(location.locationName)
                }
            )
        }
    }
}

@Composable
fun LocationItem(location: LocationEntity, onLocationSelected: (LocationEntity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .clickable { onLocationSelected(location) }, // Handle click on location
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = location.locationName,
                style = MaterialTheme.typography.headlineMedium
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite"
            )
        }
    }
}

@Composable
fun ForecastItem(forecast: Forecast) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = forecast.date,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Temp: ${forecast.temperature}°C",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Wind: ${forecast.windSpeed} km/h",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Humidity: ${forecast.humidity}%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Weather icon (optional)
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Weather Icon",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}