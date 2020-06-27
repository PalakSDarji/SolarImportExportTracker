package com.palak.solarimportexporttracker.di

import android.app.Application
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.palak.solarimportexporttracker.home.solarList.db.SolarDataDao
import com.palak.solarimportexporttracker.home.solarList.db.SolarDatabase
import com.palak.solarimportexporttracker.storage.SharedPreferencesStorage
import com.palak.solarimportexporttracker.storage.Storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import java.text.SimpleDateFormat
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
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

    @Singleton
    @Provides
    fun provideFirebaseDatabase() : FirebaseDatabase{
        return Firebase.database
    }

    @Singleton
    @Provides
    @UsersRef
    fun provideFirebaseDatabaseUsersReference(firebaseDatabase: FirebaseDatabase) : DatabaseReference{
        return firebaseDatabase.getReference("users")
    }

    @Singleton
    @Provides
    @InSDF
    fun provideInSdf() : SimpleDateFormat{
        return SimpleDateFormat("dd/MM/yyyy")
    }

    @Singleton
    @Provides
    @OutSDF
    fun provideOutSdf() : SimpleDateFormat{
        return SimpleDateFormat("MMM dd, yyyy")
    }

    @Singleton
    @Provides
    fun provideGsonBuilder() : GsonBuilder{
        return GsonBuilder()
    }

    @Singleton
    @Provides
    fun provideGson(gsonBuilder: GsonBuilder) : Gson{
        return gsonBuilder.create()
    }

    @Singleton
    @Provides
    fun provideStorage(application: Application, gson : Gson) : Storage {
        return SharedPreferencesStorage(application.applicationContext,gson)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersRef

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SolarDataRef

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InSDF

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OutSDF