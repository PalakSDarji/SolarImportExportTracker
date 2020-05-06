package com.palak.solarimportexporttracker.Utils

import android.app.Application
import android.content.Context
import com.palak.solarimportexporttracker.database.SolarDatabase
import com.palak.solarimportexporttracker.repository.SolarDataRepository
import com.palak.solarimportexporttracker.viewmodel.factory.SolarListViewModelFactory

object InjectorUtils{

    private fun getSolarDataRepository(context: Context) : SolarDataRepository{
        return SolarDataRepository(SolarDatabase.getDatabase(context.applicationContext).solarDataDao())
    }

    fun getSolarListViewModelFactory(application: Application) : SolarListViewModelFactory{
        return SolarListViewModelFactory(application, getSolarDataRepository(application.baseContext))
    }
}