package com.palak.solarimportexporttracker.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.palak.solarimportexporttracker.MyApplication
import com.palak.solarimportexporttracker.R
import com.palak.solarimportexporttracker.addData.AddSolarActivity
import com.palak.solarimportexporttracker.databinding.ActivityHomeBinding
import com.palak.solarimportexporttracker.home.login.UserManager
import com.palak.solarimportexporttracker.home.solarList.SolarListViewModel
import com.palak.solarimportexporttracker.home.solarList.SolarWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * This activity holds container for fragments.
 *
 * Created by Palak Darji
 */
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    private val solarListViewModel : SolarListViewModel by viewModels()

    @Inject
    lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        init()
    }

    private fun init() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(binding.bottomNavView, navHostFragment?.navController!!)
        binding.bottomNavView.setOnNavigationItemSelectedListener {
            item ->
            onNavDestinationSelected(item,navHostFragment.navController)
        }

        navHostFragment.navController.addOnDestinationChangedListener {
                controller, destination, arguments ->

        }

        solarListViewModel.solarDataList.observe(this, Observer {
            //fire Sync with server call if current user is logged in. always enqueue the request to work manager.
            if(userManager.isUserLoggedIn()) solarListViewModel.syncDataToFirebaseDatabase()
        })

        userManager.currentUser.observe(this, Observer {
            if(userManager.status != UserManager.LoginStatus.NO_LOGIN){
                //Means user has just logged in.
                solarListViewModel.syncDataToFirebaseDatabase()
                //Download data for this users, and merge it nicely.
                solarListViewModel.downloadData()
            }
        })

        setupNotification()
    }

    private fun setupNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_LOW)
            )
        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d(TAG, msg)
                //Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
            })
    }

    companion object {
        private const val TAG = "HomeActivity"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.solar_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.getItemId()) {
            R.id.action_add -> {
                val intent = Intent(this@HomeActivity,AddSolarActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
