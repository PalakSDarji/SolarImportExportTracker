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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.palak.solarimportexporttracker.MyApplication
import com.palak.solarimportexporttracker.R
import com.palak.solarimportexporttracker.Utils.mutateIndexed

import com.palak.solarimportexporttracker.databinding.FragmentSolarListBinding
import com.palak.solarimportexporttracker.di.InSDF
import com.palak.solarimportexporttracker.di.OutSDF
import com.palak.solarimportexporttracker.di.SdfTime
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

    private lateinit var navController: NavController

    @Inject
    @InSDF
    lateinit var inSdf : SimpleDateFormat

    @Inject
    @OutSDF
    lateinit var outSdf : SimpleDateFormat

    @Inject
    @SdfTime
    lateinit var sdfTime : SimpleDateFormat

    @Inject
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentSolarListBinding.inflate(inflater,container,false)
        adapter = SolarListAdapter(sdfTime,inSdf)
        binding.solarDataList.adapter = adapter

        navController = findNavController()
        subscribeUi()
        init()

        return binding.root
    }

    private fun init() {

        binding.solarDataList.isNestedScrollingEnabled = false

        binding.llAdd.setOnClickListener {
            navController.navigate(R.id.action_solarList_to_addSolarActivity)
        }

        binding.imgProfile.setOnClickListener {
            navController.navigate(R.id.action_solarList_to_syncList)
        }
    }

    private fun subscribeUi() {
        println("SolarDataList obj in solar: ${solarListViewModel.solarDataList}")
        solarListViewModel.solarDataList.observe(viewLifecycleOwner, Observer {
                solarDataList ->
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

        userManager.currentUser.observe(viewLifecycleOwner, Observer {
                user ->
            if (user != null) binding.user = user
        })
    }
}
