package com.example.android.trackmysleepquality.sleepdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SleepDetailViewModel(val nightId: Long, val databaseDao: SleepDatabaseDao): ViewModel() {
    var night: SleepNight? = null

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                night = databaseDao.get(nightId)
            }
        }
    }
}