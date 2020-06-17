/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.palak.solarimportexporttracker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.palak.solarimportexporttracker.home.login.LoginViewModel
import com.palak.solarimportexporttracker.home.login.facebook.FacebookLoginViewModel
import com.palak.solarimportexporttracker.home.login.google.GoogleLoginViewModel
import com.palak.solarimportexporttracker.home.solarList.SolarListViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * ViewModelFactory which uses Dagger to create the instances.
 */
class CustomViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("Unknown model class: $modelClass")
        }
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Module
abstract class ViewModelBuilderModule {

    @Binds
    abstract fun bindViewModelFactory(
        factory: CustomViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SolarListViewModel::class)
    abstract fun bindSolarListViewModel(solarListViewModel: SolarListViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GoogleLoginViewModel::class)
    abstract fun bindGoogleLoginViewModel(loginViewModel: GoogleLoginViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FacebookLoginViewModel::class)
    abstract fun bindFbLoginViewModel(loginViewModel: FacebookLoginViewModel) : ViewModel
}

@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
