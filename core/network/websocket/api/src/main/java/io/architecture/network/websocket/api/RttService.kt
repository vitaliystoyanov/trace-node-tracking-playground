package io.architecture.network.websocket.api

import io.architecture.network.websocket.model.NetworkClientTime
import io.architecture.network.websocket.model.NetworkServerTime
import kotlinx.coroutines.flow.Flow

interface RttService {
    fun sendClientTime(time: NetworkClientTime)

    fun streamServerTime(): Flow<NetworkServerTime>
}