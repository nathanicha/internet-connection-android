package com.natlwd.checkconnectionapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.natlwd.checkconnectionapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ConnectionCallback.ConnectionCallbackListener {

    private lateinit var binding: ActivityMainBinding
    private val model: MainViewModel by viewModels()
    private var connectionCallback: ConnectivityManager.NetworkCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        connectionCallback = ConnectionCallback()
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
        model.getConnectionState().observe(this, Observer<Boolean> { isConnecting ->
            if (isConnecting) {
                binding.tv.text = "Connection is available"
                binding.tv.setTextColor(ContextCompat.getColor(this, R.color.teal_200))
            } else {
                binding.tv.text = "Connection is unavailable"
                binding.tv.setTextColor(ContextCompat.getColor(this, R.color.dark_red))
            }
        })
    }

    private fun startConnectionCallback() {
        val connectivityManager: ConnectivityManager =
            application.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        connectionCallback?.let {
            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder().build(),
                it
            )
        }
    }

    private fun stopConnectionCallback() {
        val connectivityManager: ConnectivityManager =
            application.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connectionCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
    }

    override fun onConnectionChanged(isConnecting: Boolean) {
        model.getConnectionState().postValue(isConnecting)
    }
}