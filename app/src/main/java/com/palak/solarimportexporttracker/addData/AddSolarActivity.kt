package com.palak.solarimportexporttracker.addData

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.palak.solarimportexporttracker.R
import com.palak.solarimportexporttracker.Utils.hideKeyboard
import com.palak.solarimportexporttracker.Utils.snack
import com.palak.solarimportexporttracker.di.InSDF
import com.palak.solarimportexporttracker.di.OutSDF
import com.palak.solarimportexporttracker.model.SolarData
import com.palak.solarimportexporttracker.home.solarList.SolarListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_solar.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddSolarActivity : AppCompatActivity() {

    var cal = Calendar.getInstance()

    @Inject
    @InSDF
    lateinit var inSdf : SimpleDateFormat

    @Inject
    @OutSDF
    lateinit var outSdf : SimpleDateFormat

    val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            addDateToView()

            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),false).show()
        }

    val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)
        addTimeAndDateToView()
    }

    private fun addDateToView() {
        etDate.setText(outSdf.format(cal.time))
    }

    private fun addTimeAndDateToView() {
        addDateToView()
        etDate.setText(inSdf.format(cal.time))
    }

    private val addSolarViewModel : AddSolarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_solar)

        ivBack.setOnClickListener {
            finish()
        }

        btnSubmit.setOnClickListener {

            if(!isDataValid()) {
                return@setOnClickListener
            }

            val solarData = SolarData()
            //solarData.date = inSdf.format(outSdf.parse(etDate.text.toString()))
            solarData.id = UUID.randomUUID().toString()
            solarData.date = inSdf.format(cal.time)
            solarData.importdata = etImport.text.toString()
            solarData.export = etExport.text.toString()
            solarData.synced = false

            addSolarViewModel.insertSolarData(solarData).observe(this, Observer { id ->
                if(id>0){
                    hideKeyboard()
                    finish()
                }
            })
        }

        etDate.setOnClickListener {
            DatePickerDialog(this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun isDataValid(): Boolean {

        if(etDate.text.toString().isEmpty()){
            root.snack("Please enter the Date")
            return false
        }
        if(etImport.text.toString().isEmpty()) {
            root.snack("Please enter Import data")
            return false
        }
        if(etExport.text.toString().isEmpty()) {
            root.snack("Please enter Export data")
            return false
        }

        return true
    }

}