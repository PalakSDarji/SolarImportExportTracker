package com.palak.solarimportexporttracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.palak.solarimportexporttracker.model.SolarData
import com.palak.solarimportexporttracker.repository.SolarDataRepository
import kotlinx.coroutines.launch

class SolarListViewModel(application: Application, private val solarDataRepository: SolarDataRepository) : AndroidViewModel(application) {

    val solarDataList : LiveData<List<SolarData>> = solarDataRepository.fetchSolarData()

    fun insertSolarData(solarData: SolarData) {
        viewModelScope.launch{
            solarDataRepository.insertSolarData(solarData)
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("onCleared of SolarListViewModel")
    }
}