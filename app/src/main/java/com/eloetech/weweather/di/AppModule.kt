package com.eloetech.weweather.di

import android.content.Context
import androidx.room.Room
import com.eloetech.weweather.api.GeocodeApi
import dagger.Module
import dagger.Provides
import com.eloetech.weweather.api.WeatherApi
import com.eloetech.weweather.model.db.WeatherDao
import com.eloetech.weweather.model.db.WeatherDatabase
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    fun provideGeocodeApi(): GeocodeApi {
        return Retrofit.Builder()
            .baseUrl("https://geocode.maps.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodeApi::class.java)
    }

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): WeatherDatabase {
        return Room.databaseBuilder(
            appContext,
            WeatherDatabase::class.java,
            "weather_db"
        ).build()
    }

    @Provides
    fun provideWeatherDao(db: WeatherDatabase): WeatherDao {
        return db.weatherDao()
    }
}