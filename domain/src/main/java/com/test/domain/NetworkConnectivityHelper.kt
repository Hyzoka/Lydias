package com.test.domain

import kotlinx.coroutines.flow.StateFlow

interface NetworkConnectivityHelper {
    val isConnected: StateFlow<Boolean>
}