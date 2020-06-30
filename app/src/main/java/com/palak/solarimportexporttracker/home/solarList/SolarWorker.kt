package com.palak.solarimportexporttracker.home.solarList

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.database.DatabaseReference
import com.palak.solarimportexporttracker.di.SolarDataRef
import com.palak.solarimportexporttracker.home.login.UserManager
import com.palak.solarimportexporttracker.home.solarList.db.SolarDataRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class SolarWorker @WorkerInject constructor(@Assisted context : Context, @Assisted params : WorkerParameters,
                                            val solarDataRepository: SolarDataRepository,
                                            @SolarDataRef val solarDataRef: DatabaseReference,
                                            var userManager : UserManager) : CoroutineWorker(context,params) {

    override suspend fun doWork(): Result {

        return try {

            println("SYNCING, doWork ")
            println("SYNCING, solarDataRepository : $solarDataRepository")
            println("SYNCING, solarDataRef : $solarDataRef")

            GlobalScope.launch {
                val list = solarDataRepository.fetchSolarDataToSync()
                println("SYNCING, list size : $list")

                list.forEach {
                    solarData ->
                    val user = userManager.currentUser.value
                    user?.let {
                        currentUser ->
                        solarDataRepository.setSolarDataStatusToSelectedToSync(solarData)
                        solarData.synced = true
                        solarDataRef.child(currentUser.uid).child(solarData.id.toString()).setValue(solarData)
                        solarDataRepository.setSolarDataStatusToSelectedToSync(solarData)
                    } ?.run {

                        //If user is null.. return the loop.

                    }

                }

            }

            Result.success()
        }
        catch (e: Exception){
            Result.failure()
        }
    }


}