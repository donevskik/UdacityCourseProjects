/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.*
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val tonight = MutableLiveData<SleepNight?>()

    val nights = database.getAllNights()

    val nightsString = Transformations.map(nights){
        formatNights(it, application.resources)
    }

    private var _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    private var _navigateToDetailedSleepQuality = MutableLiveData<Long>()
    val navigateToDetailedSleepQuality: LiveData<Long>
        get() = _navigateToDetailedSleepQuality

    private var _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    val startButtonVisibility = Transformations.map(tonight) {
        it == null
    }

    val stopButtonVisibility = Transformations.map(tonight) {
        it != null
    }

    var clearButtonVisibility = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    init {
        initTonight()
    }

    private fun initTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    fun doneNavigating(){
        _navigateToSleepQuality.value = null
    }

    fun doneShowingSnackBar(){
        _showSnackBarEvent.value = false
    }

    private suspend fun getTonightFromDatabase(): SleepNight? = withContext(Dispatchers.IO){
        var night = database.getTonight()
        if (night?.startTimeInMillis != night?.endTimeInMillis){
            night = null
        }
        night
    }

    fun onSleepTrackingStarted(){
        viewModelScope.launch {
            val newNight = SleepNight()

            withContext(Dispatchers.IO) {
                database.insert(newNight)
            }

            tonight.value = getTonightFromDatabase()
        }
    }

    fun onSleeptrackingStopped(){
        viewModelScope.launch {
            var oldNight = tonight.value ?: return@launch

            oldNight.endTimeInMillis = System.currentTimeMillis()

            _navigateToSleepQuality.value = oldNight

            withContext(Dispatchers.IO){
                database.update(oldNight)
            }
        }
    }

    fun onClear(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database.clear()
            }
            _showSnackBarEvent.value = true
            tonight.value = null
        }
    }

    fun onSleepNightClicked(nightId: Long) {
        _navigateToDetailedSleepQuality.value = nightId
    }

    fun onDetailedSleepQualityNavigated(){
        _navigateToDetailedSleepQuality.value = null
    }
}

