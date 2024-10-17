package com.eloetech.weweather.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey @ColumnInfo(name = "location_name") val locationName: String,
    @ColumnInfo val latitude: Double,
    @ColumnInfo val longitude: Double
)