package com.palak.solarimportexporttracker.home.solarList.db

import androidx.lifecycle.LiveData
import com.palak.solarimportexporttracker.home.solarList.db.SolarDataDao
import com.palak.solarimportexporttracker.model.SolarData
import kotlinx.coroutines.*
import javax.inject.Inject

class SolarDataRepository @Inject constructor(private val solarDataDao: SolarDataDao) {
    //TODO commit.
    fun fetchSolarData() : LiveData<List<SolarData>>{
        return solarDataDao.fetchSolarData()
    }

    suspend fun fetchSolarDataToSync() : List<SolarData>{
        return withContext(Dispatchers.IO){
            solarDataDao.fetchSolarDataToSync()
        }
    }

    suspend fun setSolarDataStatusToSelectedToSync(solarData: SolarData) {
        solarData.assignedToSync = true
        return withContext(Dispatchers.IO){
            solarDataDao.updateSolarData(solarData)
        }
    }

    suspend fun insertSolarData(solarData: SolarData) : Long {
        return withContext(Dispatchers.IO){
            solarDataDao.insertSolarData(solarData)
        }
    }
}