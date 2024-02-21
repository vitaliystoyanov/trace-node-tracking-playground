package io.architecture.network.websocket.api

import io.architecture.network.websocket.api.model.NetworkClientTime
import io.architecture.network.websocket.api.model.NetworkServerTime
import kotlinx.coroutines.flow.Flow

interface RttService {
    suspend fun sendClientTime(time: NetworkClientTime)

    fun streamServerTime(): Flow<NetworkServerTime>
}