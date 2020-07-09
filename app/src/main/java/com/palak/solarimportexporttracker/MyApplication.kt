package com.palak.solarimportexporttracker

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.MultiDexApplication
import androidx.work.Configuration
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import dagger.hilt.android.HiltAndroidApp
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : MultiDexApplication(), Configuration.Provider {

    @Inject
    lateinit var workerFactory : HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)
        printHashKey(this)
    }

    fun printHashKey(pContext: Context) {
        try {
            val info: PackageInfo = pContext.getPackageManager()
                .getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("printHashKey()", "printHashKey() Hash Key: $hashKey")

            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("printHashKey()", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("printHashKey()", "printHashKey()", e)
        }
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder().setWorkerFactory(workerFactory).build()
}