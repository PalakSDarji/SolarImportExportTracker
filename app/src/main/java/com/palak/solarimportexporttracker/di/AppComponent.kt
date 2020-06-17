package com.palak.solarimportexporttracker.di

import android.app.Application
import com.palak.solarimportexporttracker.addData.AddSolarActivity
import com.palak.solarimportexporttracker.home.HomeActivity
import com.palak.solarimportexporttracker.home.login.LoginFragment
import com.palak.solarimportexporttracker.home.login.UserManager
import com.palak.solarimportexporttracker.home.login.di.UserComponent
import com.palak.solarimportexporttracker.home.login.facebook.FacebookLoginViewModel
import com.palak.solarimportexporttracker.home.login.google.GoogleLoginViewModel
import com.palak.solarimportexporttracker.home.solarList.SolarListFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelBuilderModule::class,AppModule::class,AppSubComponent::class])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance application : Application) : AppComponent
    }

    fun userComponent() : UserComponent.Factory

    fun userManager() : UserManager

    fun inject(homeActivity: HomeActivity)
    fun inject(solarListFragment: SolarListFragment)
    fun inject(googleLoginViewModel: GoogleLoginViewModel)
    fun inject(facebookLoginViewModel: FacebookLoginViewModel)
    fun inject(loginFragment: LoginFragment)
    fun inject(addSolarActivity: AddSolarActivity)
}

@Module(subcomponents = [UserComponent::class])
class AppSubComponent