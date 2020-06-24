package com.palak.solarimportexporttracker.home.login.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.palak.solarimportexporttracker.di.SolarDataRef
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class UserModule {

    @Provides
    @SolarDataRef
    fun provideFirebaseDatabaseSolarDataReference(firebaseDatabase: FirebaseDatabase) : DatabaseReference {
        return firebaseDatabase.getReference("solarData")
    }
}