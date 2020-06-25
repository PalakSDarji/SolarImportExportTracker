package com.palak.solarimportexporttracker.home.solarList

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.palak.solarimportexporttracker.di.SolarDataRef
import com.palak.solarimportexporttracker.home.login.UserManager
import com.palak.solarimportexporttracker.home.solarList.db.SolarDataRepository
import com.palak.solarimportexporttracker.model.SolarData
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

class SolarListViewModel @ViewModelInject constructor(
    application: Application, val solarDataRepository: SolarDataRepository,
    @SolarDataRef var solarDataRef : DatabaseReference) : AndroidViewModel(application) {

    val solarDataList: LiveData<List<SolarData>> = solarDataRepository.fetchSolarData()

    fun insertSolarData(solarData: SolarData): LiveData<Long> {

        val liveDataId = MutableLiveData<Long>()
        viewModelScope.launch {
            val id = solarDataRepository.insertSolarData(solarData)
            liveDataId.value = id
        }
        return liveDataId
    }

    //TODO()
    fun syncDataToFirebaseDatabase() {
        //solarDataRef....
    }


    override fun onCleared() {
        super.onCleared()
        println("onCleared of SolarListViewModel")
    }
}