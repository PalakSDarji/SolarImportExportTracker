package com.palak.solarimportexporttracker.di

import android.app.Application
import android.content.Context
import com.palak.solarimportexporttracker.home.solarList.db.SolarDataDao
import com.palak.solarimportexporttracker.home.solarList.db.SolarDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideSolarDatabase(application : Application) : SolarDatabase{
        return SolarDatabase.getDatabase(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideSolarDataDao(solarDatabase: SolarDatabase) : SolarDataDao{
        return solarDatabase.solarDataDao()
    }
}