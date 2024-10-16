package com.eloetech.weweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eloetech.weweather.ui.screen.WeatherScreen
import com.eloetech.weweather.ui.theme.WeweatherTheme
import com.eloetech.weweather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Declare the ViewModel in the activity
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeweatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    WeatherScreen(viewModel = weatherViewModel)
//                    Column {
//                        Greeting(
//                            name = "Android",
//                            modifier = Modifier.padding(innerPadding)
//                        )
//
//                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeweatherTheme {
        Greeting("Android")
    }
}