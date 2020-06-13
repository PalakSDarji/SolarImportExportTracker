package com.palak.solarimportexporttracker.addData

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.palak.solarimportexporttracker.R
import com.palak.solarimportexporttracker.Utils.InjectorUtils
import com.palak.solarimportexporttracker.Utils.hideKeyboard
import com.palak.solarimportexporttracker.model.SolarData
import com.palak.solarimportexporttracker.home.solarList.SolarListViewModel
import kotlinx.android.synthetic.main.fragment_add_solar.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddSolarFragment : Fragment() {

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

    private val solarListViewModel by activityViewModels<SolarListViewModel> {
        InjectorUtils.getSolarListViewModelFactory(requireActivity().application)
    }

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_solar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        btnSubmit.setOnClickListener {
            val solarData = SolarData()
            solarData.date = etDate.text.toString()
            solarData.importdata = etImport.text.toString()
            solarData.export = etExport.text.toString()

            solarListViewModel.insertSolarData(solarData)
            hideKeyboard()
            navController.navigateUp()
        }

        etDate.setOnClickListener {
            DatePickerDialog(requireActivity(),
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
}
