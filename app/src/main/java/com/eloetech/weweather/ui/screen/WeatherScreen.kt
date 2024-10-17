package com.eloetech.weweather.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.eloetech.weweather.R
import com.eloetech.weweather.model.DailyForecast
import com.eloetech.weweather.model.Location
import com.eloetech.weweather.model.db.LocationEntity
import com.eloetech.weweather.ui.components.PopupBox
import com.eloetech.weweather.viewmodel.WeatherViewModel
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val location = viewModel.currentLocation
    val favoriteLocations = viewModel.favoriteLocations
    val isLoading = viewModel.isLoading
    var showPopup by rememberSaveable { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp


    Box(modifier = Modifier
        .zIndex(1f)
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.safeContent)
    ) {
        Column {
            SearchComponent { address -> viewModel.loadWeatherFirstMatch(address) }

            location?.let {
                Spacer(Modifier.height(20.dp))
                CurrentWeather(location)
                Spacer(Modifier.height(10.dp))
                CurrentWeatherActions(
                    isFavorite = viewModel.isFavorite(location.name),
                    onDetailsSelection = { showPopup = true },
                    onFavoriteSelection = { viewModel.onFavoriteSelection(location) }
                )
            }

            if (isLoading) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            FavoriteLocations(
                favoriteLocations = favoriteLocations,
                onLocationSelected = { locationEntity -> viewModel.loadWeatherFirstMatch(locationEntity.locationName) }
            )
        }
    }

    location?.let {
        PopupBox(
            popupWidth = screenWidth.value*0.85f,
            popupHeight = 500f,
            showPopup = showPopup,
            onClickOutside = { showPopup = false },
        ) {
            ForecastComponent(location.daily)
        }
    }
}

@Composable
fun SearchComponent(onSearch: (String) -> Unit) {

    var address by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = Modifier
            .fillMaxWidth(),
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
fun CurrentWeather(location: Location) {
    Column {
        Text(
            text = location.name,
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(10.dp))
        val imageModifier = Modifier
            .size(30.dp)
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(R.drawable.thermometer_1843544), null, modifier = imageModifier)
                Spacer(Modifier.width(4.dp))
                Text(text = "${location.current.temperature.roundToInt()}°C")
            }
            Spacer(Modifier.width(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(R.drawable.weather_16279006), null, modifier = imageModifier)
                Spacer(Modifier.width(4.dp))
                Text(text = "${location.current.windSpeed} m/s")
            }
            Spacer(Modifier.width(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(R.drawable.humidity_9468938), null, modifier = imageModifier)
                Spacer(Modifier.width(4.dp))
                Text(text = "${location.current.humidity.roundToInt()}%")
            }
        }
    }
}

@Composable
fun CurrentWeatherActions(isFavorite: Boolean,
                          onDetailsSelection: () -> Unit,
                          onFavoriteSelection: () -> Unit) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Button(onClick = onDetailsSelection) {
            Text("Forecast 10 days", fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.width(12.dp))
        IconButton(onClick = onFavoriteSelection) {
            if (isFavorite) {
                Icon(Icons.Default.Favorite, null)
            } else {
                Icon(Icons.Default.FavoriteBorder, null)
            }
        }
    }
}

@Composable
fun ForecastComponent(dailyForecasts: List<DailyForecast>) {
    val dateWeight = 0.3f
    val sunWeight = 0.2f
    val tempWeight = 0.15f
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Forecast 10 days", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Row {
            Text("Date", Modifier.weight(dateWeight), fontWeight = FontWeight.Medium)
            Text("Max", Modifier.weight(tempWeight), fontWeight = FontWeight.Medium)
            Text("Min", Modifier.weight(tempWeight), fontWeight = FontWeight.Medium)
            Text("Sunrise", Modifier.weight(sunWeight), fontWeight = FontWeight.Medium)
            Text("Sunset", Modifier.weight(sunWeight), fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(4.dp))
        for (dailyForecast in dailyForecasts) {
            Row {
                Text(dailyForecast.formattedDateString(), Modifier.weight(dateWeight))
                Text("${dailyForecast.temperatureMax.roundToInt()}", Modifier.weight(tempWeight))
                Text("${dailyForecast.temperatureMin.roundToInt()}", Modifier.weight(tempWeight))
                Text(dailyForecast.formattedSunrise(), Modifier.weight(sunWeight))
                Text(dailyForecast.formattedSunset(), Modifier.weight(sunWeight))
            }
            Spacer(Modifier.height(2.dp))
        }
    }

}

@Composable
fun FavoriteLocations(
    favoriteLocations: List<LocationEntity>, // List of favorite locations
    onLocationSelected: (LocationEntity) -> Unit // Callback when a location is tapped
) {
    Text("Saved Locations", style = MaterialTheme.typography.titleMedium)
    LazyColumn {
        items(favoriteLocations) { location ->
            LocationItem(
                location = location,
                onLocationSelected = {
                    println("Location Selected: $location")
                    onLocationSelected(location)
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
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = location.locationName,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}