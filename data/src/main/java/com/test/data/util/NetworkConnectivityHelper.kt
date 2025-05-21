package com.test.data.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

interface NetworkConnectivityHelper {
    val isConnected: StateFlow<Boolean>
}

@SuppressLint("MissingPermission")
class DefaultNetworkConnectivityHelper @Inject constructor(
    @ApplicationContext context: Context,
) : NetworkConnectivityHelper {

    private val coroutineScope =
        CoroutineScope(Dispatchers.IO + CoroutineName("Network state checking"))
    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @OptIn(FlowPreview::class)
    override val isConnected: StateFlow<Boolean> by lazy {
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        callbackFlow {
            val networkCallback = object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(true)
                }

                override fun onLost(network: Network) {
                    trySend(false)
                }

                override fun onUnavailable() {
                    trySend(false)
                }
            }
            connectivityManager.registerDefaultNetworkCallback(networkCallback)

            awaitClose { connectivityManager.unregisterNetworkCallback(networkCallback) }
        }
            .debounce(1000L)
            .stateIn(
                coroutineScope,
                SharingStarted.WhileSubscribed(5000),
                networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true,
            )
    }
}