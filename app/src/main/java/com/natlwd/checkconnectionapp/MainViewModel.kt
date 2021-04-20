package com.natlwd.checkconnectionapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val isConnecting: MutableLiveData<Boolean> = MutableLiveData()

    fun getConnectionState() = isConnecting
}