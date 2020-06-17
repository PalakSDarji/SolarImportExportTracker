package com.palak.solarimportexporttracker.home.solarList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.palak.solarimportexporttracker.di.SolarDataRef
import com.palak.solarimportexporttracker.home.solarList.db.SolarDataRepository
import com.palak.solarimportexporttracker.model.SolarData
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class SolarListViewModel @Inject constructor(application: Application, val solarDataRepository: SolarDataRepository,
                                             @Named("SolarDataRef") var solarDataRef : DatabaseReference) : AndroidViewModel(application) {
/*
    @Inject
    @field:SolarDataRef
    lateinit var solarDataRef : DatabaseReference*/

    val solarDataList : LiveData<List<SolarData>> = solarDataRepository.fetchSolarData()

    fun insertSolarData(solarData: SolarData) {
        viewModelScope.launch{
            solarDataRepository.insertSolarData(solarData)
        }
    }

    //TODO()
    fun syncDataToFirebaseDatabase(){
        //solarDataRef....
    }


    override fun onCleared() {
        super.onCleared()
        println("onCleared of SolarListViewModel")
    }
}