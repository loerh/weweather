package com.eloetech.weweather.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Query("SELECT * FROM locations")
    suspend fun getAllFavorites(): List<LocationEntity>

    @Query("SELECT * FROM locations WHERE location_name = :locationName")
    suspend fun getFavorite(locationName: String): LocationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationEntity: LocationEntity)

    @Delete
    suspend fun delete(locationEntity: LocationEntity)
}