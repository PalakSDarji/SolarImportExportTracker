package com.palak.solarimportexporttracker.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.palak.solarimportexporttracker.dao.SolarDataDao
import com.palak.solarimportexporttracker.model.SolarData

class SolarDataRepository(private val solarDataDao: SolarDataDao) {

    val solarDataList : LiveData<List<SolarData>> = solarDataDao.fetchSolarData()

    suspend fun insertSolarData(solarData: SolarData){
        solarDataDao.insertSolarData(solarData)
    }
}