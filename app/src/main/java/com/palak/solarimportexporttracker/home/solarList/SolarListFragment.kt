package com.palak.solarimportexporttracker.home.solarList

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.palak.solarimportexporttracker.MyApplication
import com.palak.solarimportexporttracker.Utils.mutateIndexed

import com.palak.solarimportexporttracker.databinding.FragmentSolarListBinding
import com.palak.solarimportexporttracker.home.login.UserManager
import com.palak.solarimportexporttracker.home.solarList.model.DateHeader
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

/**
 * This screen shows list of import exports of solar power date wise.
 */
@AndroidEntryPoint
class SolarListFragment : Fragment() {

    private lateinit var binding : FragmentSolarListBinding

    private val solarListViewModel : SolarListViewModel by activityViewModels()

    private lateinit var adapter : SolarListAdapter

    private val inSdf = SimpleDateFormat("dd/MM/yyyy")
    private val outSdf = SimpleDateFormat("MMM dd, yyyy")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentSolarListBinding.inflate(inflater,container,false)
        adapter = SolarListAdapter()
        binding.solarDataList.adapter = adapter

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        solarListViewModel.solarDataList.observe(viewLifecycleOwner, Observer {
                solarDataList ->

            println("SolarDataList: $solarDataList")
            binding.hasData = !solarDataList.isNullOrEmpty()

            val modifiedListWithHeader = mutableListOf<Any>()
            solarDataList.forEachIndexed { index, solarData ->

                val date = outSdf.format(inSdf.parse(solarData.date))

                if(!modifiedListWithHeader.contains(DateHeader(date))){
                    modifiedListWithHeader.add(DateHeader(date))
                }
                modifiedListWithHeader.add(solarData)
            }
            adapter.submitList(modifiedListWithHeader)
        })
    }
}
