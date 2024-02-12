package io.architecture.playground.data.remote.interfaces

import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.remote.model.NetworkClientTime
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.data.remote.model.NetworkServerTime
import io.architecture.playground.data.remote.model.NetworkTrace
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow

interface NetworkDataSource {
    fun streamTraces(): ReceiveChannel<NetworkTrace>

    fun streamRoutes(): Flow<NetworkRoute>

    fun streamTraceConnectionState(): Flow<ConnectionEvent>

    fun streamRouteConnectionState(): Flow<ConnectionEvent>

    fun sendClientTime(time: NetworkClientTime)

    fun streamServerTime(): Flow<NetworkServerTime>

}