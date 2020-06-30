package com.palak.solarimportexporttracker.home.solarList

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.database.DatabaseReference
import com.palak.solarimportexporttracker.di.SolarDataRef
import com.palak.solarimportexporttracker.home.login.UserManager
import com.palak.solarimportexporttracker.home.solarList.db.SolarDataRepository
import com.palak.solarimportexporttracker.model.SolarData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
class SolarListViewModel @ViewModelInject constructor(val app: Application,
                                                      solarDataRepository: SolarDataRepository,
                                                      var userManager : UserManager)
    : AndroidViewModel(app) {

    init {
        println("SOLARVM inited")
    }
    val solarDataList: LiveData<List<SolarData>> = solarDataRepository.fetchSolarData()

    fun syncDataToFirebaseDatabase() {
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(SolarWorker::class.java).build()
        WorkManager.getInstance(app).enqueue(oneTimeWorkRequest)
    }

    override fun onCleared() {
        super.onCleared()
        println("SOLARVM onCleared of SolarListViewModel")
    }

    fun downloadData() {
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(GetSolarDataWorker::class.java).build()
        WorkManager.getInstance(app).enqueue(oneTimeWorkRequest)
    }
}