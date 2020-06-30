package com.palak.solarimportexporttracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solar_data")
data class SolarData(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "date")
    var date: String = "",

    @ColumnInfo(name = "importdata")
    var importdata: String = "",

    @ColumnInfo(name = "export")
    var export: String = "",

    @ColumnInfo(name = "assignedToSync")
    var assignedToSync: Boolean = false,

    @ColumnInfo(name = "synced")
    var synced: Boolean = false
)