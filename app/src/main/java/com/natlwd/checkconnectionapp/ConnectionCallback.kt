package com.natlwd.checkconnectionapp

import android.net.ConnectivityManager
import android.net.Network

object ConnectionCallback : ConnectivityManager.NetworkCallback() {

    var listener: ConnectionCallbackListener? = null

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
}