package io.architecture.datasource.api

import io.architecture.api.model.NetworkClientTime
import io.architecture.api.model.NetworkRoute
import io.architecture.api.model.NetworkServerTime
import io.architecture.api.model.NetworkTrace
import io.architecture.model.ConnectionEvent
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