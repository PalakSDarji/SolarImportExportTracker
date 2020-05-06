package com.palak.solarimportexporttracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.palak.solarimportexporttracker.R
import com.palak.solarimportexporttracker.adapter.SolarListAdapter.*
import com.palak.solarimportexporttracker.databinding.ListItemSolarDataBinding
import com.palak.solarimportexporttracker.model.SolarData

class SolarListAdapter : ListAdapter<SolarData, ViewHolder>(SolarDataDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_solar_data, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ListItemSolarDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(solarDataItem : SolarData){
            with(binding){
                solarData = solarDataItem
                executePendingBindings()
            }
        }
    }

}


private class SolarDataDiffCallback : DiffUtil.ItemCallback<SolarData>(){
    override fun areItemsTheSame(oldItem: SolarData, newItem: SolarData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SolarData, newItem: SolarData): Boolean {
        return oldItem == newItem
    }

}