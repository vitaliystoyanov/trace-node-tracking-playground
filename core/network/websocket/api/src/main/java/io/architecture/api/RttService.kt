package io.architecture.api

import io.architecture.api.model.NetworkClientTime
import io.architecture.api.model.NetworkServerTime
import kotlinx.coroutines.flow.Flow

interface RttService {
    fun sendClientTime(time: NetworkClientTime)

    fun streamServerTime(): Flow<NetworkServerTime>
}