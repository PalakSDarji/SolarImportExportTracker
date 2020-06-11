package com.palak.solarimportexporttracker.Utils

import android.app.Application
import android.content.Context
import com.palak.solarimportexporttracker.solarData.SolarDatabase
import com.palak.solarimportexporttracker.solarData.SolarDataRepository
import com.palak.solarimportexporttracker.solarData.SolarListViewModelFactory

object InjectorUtils{

    private fun getSolarDataRepository(context: Context) : SolarDataRepository {
        return SolarDataRepository(
            SolarDatabase.getDatabase(context.applicationContext).solarDataDao()
        )
    }

    fun getSolarListViewModelFactory(application: Application) : SolarListViewModelFactory {
        return SolarListViewModelFactory(
            application,
            getSolarDataRepository(application.baseContext)
        )
    }
}