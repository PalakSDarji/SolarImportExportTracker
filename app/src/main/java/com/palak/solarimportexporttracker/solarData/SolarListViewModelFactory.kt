package com.palak.solarimportexporttracker.solarData

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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