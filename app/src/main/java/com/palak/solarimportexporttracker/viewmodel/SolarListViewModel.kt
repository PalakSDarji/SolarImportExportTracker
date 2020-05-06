package com.palak.solarimportexporttracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.palak.solarimportexporttracker.model.SolarData
import com.palak.solarimportexporttracker.repository.SolarDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SolarListViewModel(application: Application, private val solarDataRepository: SolarDataRepository) : AndroidViewModel(application) {

    lateinit var solarDataList : LiveData<List<SolarData>>

    fun fetchSolarDataList() : LiveData<List<SolarData>>{

        solarDataList = solarDataRepository.solarDataList
        return solarDataList
    }

    fun insertSolarData(solarData: SolarData) = viewModelScope.launch(Dispatchers.IO){
        solarDataRepository.insertSolarData(solarData)
    }
}