package com.eloetech.weweather.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eloetech.weweather.R
import com.eloetech.weweather.model.DailyForecast
import com.eloetech.weweather.model.Location
import com.eloetech.weweather.model.db.LocationEntity
import com.eloetech.weweather.viewmodel.WeatherViewModel
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val location = viewModel.currentLocation
    val favoriteLocations = viewModel.favoriteLocations
    val isLoading = viewModel.isLoading

    Column {
        SearchComponent { address -> viewModel.loadWeatherFirstMatch(address) }

        location?.let {
            WeatherDetails(location)
            Spacer(Modifier.height(8.dp))
            ForecastComponent(location.daily)
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant)
        }

        FavoriteLocations(favoriteLocations, viewModel)
    }
}

@Composable
fun SearchComponent(onSearch: (String) -> Unit) {

    var address by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeContent),
        value = address,
        onValueChange = { address = it },
        label = { Text("Search Place") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearch(address)
            }
        ),
        trailingIcon = {
            IconButton(onClick = { onSearch(address) }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}

@Composable
fun WeatherDetails(location: Location) {
    Column {
        Text(text = location.name, fontSize = 24.sp)
        Spacer(Modifier.height(4.dp))
        val imageModifier = Modifier
            .size(40.dp)
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Row {
                Image(painter = painterResource(R.drawable.thermometer_1843544), null, modifier = imageModifier)
                Text(text = "${location.current.temperature}°C")
            }
            Spacer(Modifier.width(8.dp))
            Row {
                Image(painter = painterResource(R.drawable.weather_16279006), null, modifier = imageModifier)
                Text(text = "${location.current.windSpeed} m/s")
            }
            Spacer(Modifier.width(8.dp))
            Row {
                Image(painter = painterResource(R.drawable.humidity_9468938), null, modifier = imageModifier)
                Text(text = "${location.current.humidity}%")
            }
        }
    }
}

@Composable
fun ForecastComponent(dailyForecasts: List<DailyForecast>) {
    val weight = 0.2f
    Column {
        Text(text = "Forecast 10 days", fontSize = 18.sp)
        Row {
            Text("Date", Modifier.weight(weight))
            Text("Max", Modifier.weight(weight))
            Text("Min", Modifier.weight(weight))
            Text("Sunrise", Modifier.weight(weight))
            Text("Sunset", Modifier.weight(weight))
        }
        for (dailyForecast in dailyForecasts) {
            Row {
                Text(dailyForecast.formattedDateString(), Modifier.weight(weight))
                Text("${dailyForecast.temperatureMax.roundToInt()}", Modifier.weight(weight))
                Text("${dailyForecast.temperatureMin.roundToInt()}", Modifier.weight(weight))
                Text(dailyForecast.formattedSunrise(), Modifier.weight(weight))
                Text(dailyForecast.formattedSunset(), Modifier.weight(weight))
            }
        }
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
