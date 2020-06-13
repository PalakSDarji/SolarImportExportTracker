package com.palak.solarimportexporttracker.di

import android.app.Application
import com.palak.solarimportexporttracker.home.HomeActivity
import com.palak.solarimportexporttracker.home.solarList.SolarListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelBuilderModule::class,AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance application : Application) : AppComponent
    }

    fun inject(homeActivity: HomeActivity)
    fun inject(solarListFragment: SolarListFragment)
}