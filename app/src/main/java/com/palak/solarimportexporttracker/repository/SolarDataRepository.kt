package com.palak.solarimportexporttracker.repository

import androidx.lifecycle.LiveData
import com.palak.solarimportexporttracker.dao.SolarDataDao
import com.palak.solarimportexporttracker.model.SolarData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SolarDataRepository(private val solarDataDao: SolarDataDao) {

    fun fetchSolarData() : LiveData<List<SolarData>>{
        return solarDataDao.fetchSolarData()
    }

    suspend fun insertSolarData(solarData: SolarData){
        withContext(Dispatchers.IO){
            solarDataDao.insertSolarData(solarData)
        }
    }
}