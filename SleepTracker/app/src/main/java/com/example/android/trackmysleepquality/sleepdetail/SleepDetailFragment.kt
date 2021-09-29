package com.example.android.trackmysleepquality.sleepdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepDetailBinding


class SleepDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentSleepDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_detail, container, false)

        val aplication = requireNotNull(this.activity).application

        val args = SleepDetailFragmentArgs.fromBundle(arguments!!)

        val database = SleepDatabase.getInstance(aplication)

        val sleepDetailViewModelFactory = SleepDetailViewModelFactory(args.nightId,database.sleepDatabaseDao)

        val sleepDetailViewModel = ViewModelProvider(this, sleepDetailViewModelFactory).get(SleepDetailViewModel::class.java)

        binding.sleepDetailViewmodel = sleepDetailViewModel

        // Inflate the layout for this fragment
        return binding.root
    }


}