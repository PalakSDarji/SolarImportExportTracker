package com.palak.solarimportexporttracker.home.solarList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.palak.solarimportexporttracker.home.solarList.db.SolarDataRepository

class SolarListViewModelFactory(
    private val application : Application,
    private val solarDataRepository: SolarDataRepository
) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SolarListViewModel(
            application,
            solarDataRepository
        ) as T
    }
}