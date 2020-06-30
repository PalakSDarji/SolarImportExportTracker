package com.palak.solarimportexporttracker.home.solarList

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.palak.solarimportexporttracker.di.SolarDataRef
import com.palak.solarimportexporttracker.home.login.UserManager
import com.palak.solarimportexporttracker.home.solarList.db.SolarDataRepository
import com.palak.solarimportexporttracker.model.SolarData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GetSolarDataWorker @WorkerInject constructor(@Assisted context : Context, @Assisted params : WorkerParameters,
                                                  val solarDataRepository: SolarDataRepository,
                                                  @SolarDataRef val solarDataRef: DatabaseReference,
                                                  var userManager : UserManager) : CoroutineWorker(context,params) {
    override suspend fun doWork(): Result {

        return try{

            val user = userManager.currentUser.value
            user?.let { currentUser ->

                println("GETSOLARDATAWORKER currentUser uid : ${currentUser.uid}")
                solarDataRef.child(currentUser.uid).addListenerForSingleValueEvent(object : ValueEventListener{

                    override fun onCancelled(databaseError: DatabaseError) {
                        Result.failure()
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        println("GETSOLARDATAWORKER dataSnapshot : ${dataSnapshot}")
                        GlobalScope.launch {
                            dataSnapshot.children.forEach {
                                println("GETSOLARDATAWORKER dataSnapshot it: $it")
                                val solarData : SolarData? = it.getValue(SolarData::class.java)
                                println("GETSOLARDATAWORKER dataSnapshot solarData: $solarData")
                                solarData?.let {
                                        it1 ->
                                    solarDataRepository.insertSolarData(it1)
                                }
                            }
                        }
                    }
                })
            }

            Result.success()
        }
        catch (e : Exception){
            e.printStackTrace()
            Result.failure()
        }
    }
}