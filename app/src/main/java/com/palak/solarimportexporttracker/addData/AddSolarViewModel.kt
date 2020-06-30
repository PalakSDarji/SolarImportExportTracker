package com.palak.solarimportexporttracker.addData

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.palak.solarimportexporttracker.home.login.UserManager
import com.palak.solarimportexporttracker.home.solarList.db.SolarDataRepository
import com.palak.solarimportexporttracker.model.SolarData
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.launch

@ActivityScoped
class AddSolarViewModel @ViewModelInject constructor(app : Application,
                                                     private val solarDataRepository: SolarDataRepository,
                                                     var userManager : UserManager
) : AndroidViewModel(app) {

    init{
        println("AddSolarViewModel inited")
    }

    fun insertSolarData(solarData: SolarData): LiveData<Long> {

        val liveDataId = MutableLiveData<Long>()
        viewModelScope.launch {
            val id = solarDataRepository.insertSolarData(solarData)
            liveDataId.value = id
        }

        return liveDataId
    }

    override fun onCleared() {
        super.onCleared()
        println("AddSolarViewModel cleared")
    }
}