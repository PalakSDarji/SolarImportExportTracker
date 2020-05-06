package com.palak.solarimportexporttracker.ui.fragments

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
import com.palak.solarimportexporttracker.model.SolarData
import com.palak.solarimportexporttracker.viewmodel.SolarListViewModel
import kotlinx.android.synthetic.main.fragment_add_solar.*

/**
 * A simple [Fragment] subclass.
 */
class AddSolarFragment : Fragment() {

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
            solarData.import = etImport.text.toString()
            solarData.export = etExport.text.toString()

            solarListViewModel.insertSolarData(solarData)
            navController.navigateUp()
        }
    }
}
