package com.palak.solarimportexporttracker.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.palak.solarimportexporttracker.repository.SolarDataRepository
import com.palak.solarimportexporttracker.viewmodel.SolarListViewModel

class SolarListViewModelFactory(
    private val application : Application,
    private val solarDataRepository: SolarDataRepository) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SolarListViewModel(application,solarDataRepository) as T
    }
}