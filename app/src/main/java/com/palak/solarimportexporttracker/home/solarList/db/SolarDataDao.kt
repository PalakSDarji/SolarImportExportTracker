package com.palak.solarimportexporttracker.home.solarList.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.palak.solarimportexporttracker.model.SolarData
import kotlinx.coroutines.Deferred

@Dao
interface SolarDataDao {

    @Query("SELECT * from solar_data ORDER BY date DESC")
    fun fetchSolarData() : LiveData<List<SolarData>>

    @Query("SELECT * from solar_data where assignedToSync = 0 ORDER BY date DESC")
    suspend fun fetchSolarDataToSync() : List<SolarData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSolarData(solarData: SolarData) : Long

    @Update
    suspend fun updateSolarData(solarData: SolarData)
}