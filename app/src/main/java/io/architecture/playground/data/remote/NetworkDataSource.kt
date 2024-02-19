package io.architecture.playground.data.remote

import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.remote.model.NetworkClientTime
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.data.remote.model.NetworkServerTime
import io.architecture.playground.data.remote.model.NetworkTrace
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