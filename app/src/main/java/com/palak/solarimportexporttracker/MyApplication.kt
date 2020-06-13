package com.palak.solarimportexporttracker

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.palak.solarimportexporttracker.di.AppComponent
import com.palak.solarimportexporttracker.di.DaggerAppComponent
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MyApplication : MultiDexApplication() {

    val appComponent : AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

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
}