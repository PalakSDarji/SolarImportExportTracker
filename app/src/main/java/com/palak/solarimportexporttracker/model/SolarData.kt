package com.palak.solarimportexporttracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solar_data")
data class SolarData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0,
    @ColumnInfo(name = "date")
    var date : String = "",
    @ColumnInfo(name = "importdata")
    var import: String = "",
    @ColumnInfo(name = "export")
    var export: String = "")