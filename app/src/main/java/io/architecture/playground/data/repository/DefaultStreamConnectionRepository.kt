package io.architecture.playground.data.repository

import io.architecture.playground.data.remote.interfaces.NetworkDataSource
import io.architecture.playground.data.remote.model.ConnectionEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

open class DefaultStreamConnectionRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) {

    private val skipMessageReceived =
        { event: ConnectionEvent -> event != ConnectionEvent.MESSAGE_RECEIVED }

    fun streamTraceConnectionEvents(): Flow<ConnectionEvent> =
        networkDataSource.streamTraceConnectionState()
            .filter(skipMessageReceived)

    fun streamRouteConnectionEvents(): Flow<ConnectionEvent> =
        networkDataSource.streamRouteConnectionState()
            .filter(skipMessageReceived)
}