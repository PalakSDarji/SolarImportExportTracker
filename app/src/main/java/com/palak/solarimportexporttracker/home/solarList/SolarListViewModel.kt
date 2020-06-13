package com.palak.solarimportexporttracker.home.solarList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.palak.solarimportexporttracker.home.solarList.db.SolarDataRepository
import com.palak.solarimportexporttracker.model.SolarData
import kotlinx.coroutines.launch
import javax.inject.Inject

class SolarListViewModel @Inject constructor(application: Application, val solarDataRepository: SolarDataRepository) : AndroidViewModel(application) {

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