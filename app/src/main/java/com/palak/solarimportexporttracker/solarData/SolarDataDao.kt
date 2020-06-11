package com.palak.solarimportexporttracker.solarData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.palak.solarimportexporttracker.model.SolarData

@Dao
interface SolarDataDao {

    @Query("SELECT * from solar_data ORDER BY date DESC")
    fun fetchSolarData() : LiveData<List<SolarData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSolarData(solarData: SolarData)
}