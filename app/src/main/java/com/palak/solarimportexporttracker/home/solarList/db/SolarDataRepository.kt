package com.palak.solarimportexporttracker.home.solarList.db

import androidx.lifecycle.LiveData
import com.palak.solarimportexporttracker.home.solarList.db.SolarDataDao
import com.palak.solarimportexporttracker.model.SolarData
import kotlinx.coroutines.*
import javax.inject.Inject

class SolarDataRepository @Inject constructor(val solarDataDao: SolarDataDao) {

    fun fetchSolarData() : LiveData<List<SolarData>>{
        return solarDataDao.fetchSolarData()
    }

    suspend fun insertSolarData(solarData: SolarData) : Long {
        return withContext(Dispatchers.IO){
            solarDataDao.insertSolarData(solarData)
        }
    }
}