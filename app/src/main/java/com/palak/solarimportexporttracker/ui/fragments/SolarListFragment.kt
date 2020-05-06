package com.palak.solarimportexporttracker.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.palak.solarimportexporttracker.R

import com.palak.solarimportexporttracker.Utils.InjectorUtils
import com.palak.solarimportexporttracker.adapter.SolarListAdapter
import com.palak.solarimportexporttracker.databinding.FragmentSolarListBinding
import com.palak.solarimportexporttracker.viewmodel.SolarListViewModel
import java.util.*

/**
 * This screen shows list of import exports of solar power date wise.
 */
class SolarListFragment : Fragment() {

    private lateinit var binding : FragmentSolarListBinding
    private val solarListViewModel by activityViewModels<SolarListViewModel> {
        InjectorUtils.getSolarListViewModelFactory(requireActivity().application)
    }
    private lateinit var adapter : SolarListAdapter
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentSolarListBinding.inflate(inflater,container,false)
        adapter = SolarListAdapter()
        binding.solarDataList.adapter = adapter
        navController = findNavController()

        subscribeUi()
        setClickListeners()

        return binding.root
    }

    private fun setClickListeners() {
        binding.btnAdd.setOnClickListener {
            navController.navigate(R.id.action_solarListFragment_to_addSolarFragment)
        }
    }

    private fun subscribeUi() {
        solarListViewModel.fetchSolarDataList().observe(viewLifecycleOwner, Observer {
                solarDataList ->
                println("SolarDataList: $solarDataList")
                binding.hasData = !solarDataList.isNullOrEmpty()
                adapter.submitList(solarDataList)
        })
    }
}
