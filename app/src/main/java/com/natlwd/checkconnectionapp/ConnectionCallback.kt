package com.natlwd.checkconnectionapp

import android.net.ConnectivityManager
import android.net.Network

class ConnectionCallback : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        listener?.onConnectionChanged(true)
    }


    override fun onLost(network: Network) {
        super.onLost(network)
        listener?.onConnectionChanged(false)

    }

    interface ConnectionCallbackListener {
        fun onConnectionChanged(isConnecting: Boolean)
    }

    companion object {
        var listener: ConnectionCallbackListener? = null
    }
}