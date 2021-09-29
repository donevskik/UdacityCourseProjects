package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.GridListItemSleepQualityBinding

class SleepNightAdapter(val sleepNightListener: SleepNightListener) : ListAdapter<SleepNight, SleepNightAdapter.SleepNightViewHolder>(SleepNightDiffCallback()) {
    override fun onBindViewHolder(holder: SleepNightViewHolder, position: Int) {
        holder.bind(getItem(position)!!, sleepNightListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepNightViewHolder {
        return SleepNightViewHolder.from(parent)
    }

    class SleepNightViewHolder private constructor(val binding: GridListItemSleepQualityBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(sleepNight: SleepNight, sleepNightListener: SleepNightListener) {
            binding.sleep = sleepNight
            binding.clicklistener = sleepNightListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SleepNightViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GridListItemSleepQualityBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return SleepNightViewHolder(binding)
            }
        }
    }
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>(){
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }

}

class SleepNightListener(val clickListener: (sleepid: Long) -> Unit){
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}