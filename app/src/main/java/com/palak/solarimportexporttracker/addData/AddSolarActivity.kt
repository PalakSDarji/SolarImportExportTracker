package com.palak.solarimportexporttracker.addData

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.palak.solarimportexporttracker.R
import com.palak.solarimportexporttracker.Utils.InjectorUtils
import com.palak.solarimportexporttracker.Utils.hideKeyboard
import com.palak.solarimportexporttracker.model.SolarData
import com.palak.solarimportexporttracker.home.solarList.SolarListViewModel
import kotlinx.android.synthetic.main.activity_add_solar.*
import java.text.SimpleDateFormat
import java.util.*


class AddSolarActivity : AppCompatActivity() {

    var cal = Calendar.getInstance()

    val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            addDateToView()
        }

    private fun addDateToView() {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        etDate.setText(simpleDateFormat.format(cal.time))
    }

    private val solarListViewModel by viewModels<SolarListViewModel> {
        InjectorUtils.getSolarListViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_solar)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true);
            it.setDisplayShowHomeEnabled(true);
            it.title = "Add Data"
        }

        btnSubmit.setOnClickListener {
            val solarData = SolarData()
            solarData.date = etDate.text.toString()
            solarData.importdata = etImport.text.toString()
            solarData.export = etExport.text.toString()

            solarListViewModel.insertSolarData(solarData)
            hideKeyboard()
            finish()
        }

        etDate.setOnClickListener {
            DatePickerDialog(this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}