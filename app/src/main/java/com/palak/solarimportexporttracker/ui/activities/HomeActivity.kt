package com.palak.solarimportexporttracker.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.palak.solarimportexporttracker.R

/**
 * This activity holds container for fragments.
 *
 * Created by Palak Darji
 */
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}
