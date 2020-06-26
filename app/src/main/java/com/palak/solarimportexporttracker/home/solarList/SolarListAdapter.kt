package com.palak.solarimportexporttracker.home.solarList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.palak.solarimportexporttracker.R
import com.palak.solarimportexporttracker.databinding.ItemDateBinding
import com.palak.solarimportexporttracker.home.solarList.SolarListAdapter.*
import com.palak.solarimportexporttracker.databinding.ListItemSolarDataBinding
import com.palak.solarimportexporttracker.home.solarList.model.DateHeader
import com.palak.solarimportexporttracker.model.SolarData

class SolarListAdapter : ListAdapter<Any, RecyclerView.ViewHolder>(SolarDataDiffCallback()) {

    companion object {
        private const val TYPE_DATE = 0
        private const val TYPE_SOLAR_DATA = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == TYPE_DATE){
            return DateViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_date, parent, false
                )
            )
        }
        else {
            return SolarViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_solar_data, parent, false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(getItem(position) is DateHeader){
            return TYPE_DATE
        }
        return TYPE_SOLAR_DATA
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItem(position) is DateHeader){
            holder as DateViewHolder
            holder.bind(getItem(position) as DateHeader)
        }
        else{
            holder as SolarViewHolder
            holder.bind(getItem(position) as SolarData)
        }
    }

    class DateViewHolder(private val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dateHeader : DateHeader){
            with(binding){
                date = dateHeader
                executePendingBindings()
            }
        }
    }

    class SolarViewHolder(private val binding: ListItemSolarDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(solarDataItem : SolarData){
            with(binding){
                solarData = solarDataItem
                executePendingBindings()
                val exportData = solarDataItem.export.toInt()
                val importData = solarDataItem.importdata.toInt()
                if(exportData < importData){
                    val diff =  importData - exportData
                    diff.also {
                        binding.tvDiff.visibility = View.VISIBLE
                        if (it == 1){
                            binding.tvDiff.text = "Your usage is $it Unit higher."
                        }
                        else{
                            binding.tvDiff.text = "Your usage is $it Units higher."
                        }
                    }
                }
                else if(importData < exportData){
                    val diff =  exportData - importData
                    diff.also {
                        binding.tvDiff.visibility = View.VISIBLE
                        if (it == 1){
                            binding.tvDiff.text = "Your usage is $it Unit lower."
                        }
                        else{
                            binding.tvDiff.text = "Your usage is $it Units lower."
                        }
                    }
                }
                else{
                    binding.tvDiff.text = ""
                    binding.tvDiff.visibility = View.GONE
                }
            }
        }
    }
}

private class SolarDataDiffCallback : DiffUtil.ItemCallback<Any>(){
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        if(oldItem is DateHeader && newItem is DateHeader){
            return oldItem.date == newItem.date
        }
        else if(oldItem is SolarData && newItem is SolarData){
            return oldItem.id == newItem.id
        }
        return false
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        if(oldItem is DateHeader && newItem is DateHeader){
            return oldItem == newItem
        }
        else if(oldItem is SolarData && newItem is SolarData){
            return oldItem == newItem
        }
        return false
    }

}