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

package com.palak.solarimportexporttracker.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.palak.solarimportexporttracker.model.User
import com.palak.solarimportexporttracker.storage.Storage
import javax.inject.Inject
import kotlin.reflect.KClass

class SharedPreferencesStorage @Inject constructor(context: Context, val gson : Gson) : Storage {
/*

    @Inject
    lateinit var gson: Gson
*/

    private val sharedPreferences = context.getSharedPreferences("SolarImportExportTracker", Context.MODE_PRIVATE)

    override fun setString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun getString(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }

    override fun <T> put(key: String, value: T){
        val jsonString = gson.toJson(value)
        setString(key,jsonString)
    }

    override fun <T> get(key: String, clazz: Class<T>) : T?{
        val jsonString = getString(key)
        return gson.fromJson(jsonString,clazz)
    }
}
