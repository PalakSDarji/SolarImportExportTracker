package com.palak.solarimportexporttracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.palak.solarimportexporttracker.dao.SolarDataDao
import com.palak.solarimportexporttracker.model.SolarData

@Database(entities = arrayOf(SolarData::class), version = 1, exportSchema = false)
public abstract class SolarDatabase : RoomDatabase() {

    abstract fun solarDataDao() : SolarDataDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: SolarDatabase? = null

        fun getDatabase(context: Context): SolarDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SolarDatabase::class.java,
                    "solar_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}