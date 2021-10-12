package com.natlwd.checkconnectionapp

import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.natlwd.checkconnectionapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ConnectionCallback.ConnectionCallbackListener {

    private lateinit var binding: ActivityMainBinding
    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ConnectionCallback.listener = this
        setupObserve()
    }

    override fun onResume() {
        super.onResume()
        startConnectionCallback()
    }

    override fun onStop() {
        super.onStop()
        stopConnectionCallback()
    }

    private fun setupObserve() {
        //First Solution
        model.getConnectionState().observe(this, Observer<Boolean> { isConnecting ->
            if (isConnecting) {
                binding.firstConnectionTv.text = "Connection is available"
                binding.firstConnectionTv.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.holo_green_light
                    )
                )
            } else {
                binding.firstConnectionTv.text = "Connection is unavailable"
                binding.firstConnectionTv.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.dark_red
                    )
                )
            }
        })

        //Second Solution
        ConnectionLiveData(this).observe(this, Observer<Boolean> { isConnecting ->
            if (isConnecting) {
                binding.secondConnectionTv.text = "Connection is available"
                binding.secondConnectionTv.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.holo_green_light
                    )
                )
            } else {
                binding.secondConnectionTv.text = "Connection is unavailable"
                binding.secondConnectionTv.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.dark_red
                    )
                )
            }
        })
    }

    private fun startConnectionCallback() {
        val connectivityManager: ConnectivityManager =
            application.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        ConnectionCallback.let {
            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder().build(),
                it
            )
        }
    }

    private fun stopConnectionCallback() {
        val connectivityManager: ConnectivityManager =
            application.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        ConnectionCallback.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
    }

    override fun onConnectionChanged(isConnecting: Boolean) {
        model.getConnectionState().postValue(isConnecting)
    }
}