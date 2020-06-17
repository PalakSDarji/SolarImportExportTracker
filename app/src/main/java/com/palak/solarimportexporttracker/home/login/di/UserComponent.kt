package com.palak.solarimportexporttracker.home.login.di

import com.palak.solarimportexporttracker.home.HomeActivity
import com.palak.solarimportexporttracker.home.login.LoginFragment
import com.palak.solarimportexporttracker.home.solarList.SolarListFragment
import dagger.Subcomponent

@Subcomponent(modules = [UserModule::class])
interface UserComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create() : UserComponent
    }

    fun inject(loginFragment: LoginFragment)
    fun inject(solarListFragment: SolarListFragment)
    fun inject(homeActivity: HomeActivity)
}