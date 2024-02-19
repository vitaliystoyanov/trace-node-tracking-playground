package io.architecture.datasource.api

import io.architecture.model.ConnectionEvent
import io.architecture.network.websocket.api.model.NetworkRoute
import io.architecture.network.websocket.api.model.NetworkTrace
import io.architecture.network.websocket.model.NetworkClientTime
import io.architecture.network.websocket.model.NetworkServerTime
import kotlinx.coroutines.flow.Flow

interface NetworkDataSource {

    fun openSession()

    fun closeSession()

    fun streamTraces(): Flow<NetworkTrace>

    fun streamRoutes(): Flow<NetworkRoute>

    fun sendClientTime(time: NetworkClientTime)

    fun streamServerTime(): Flow<NetworkServerTime>

    fun streamConnectionEvents(): Flow<ConnectionEvent>

}