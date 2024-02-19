package io.architecture.playground.data.remote.websocket

import com.tinder.scarlet.WebSocket
import io.architecture.playground.data.remote.model.NetworkClientTime
import io.architecture.playground.data.remote.model.NetworkServerTime
import kotlinx.coroutines.flow.Flow

interface RttService {
    fun sendClientTime(time: NetworkClientTime)

    fun streamServerTime(): Flow<NetworkServerTime>
}