package io.architecture.playground.data.remote

import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.remote.model.NetworkClientTime
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.data.remote.model.NetworkServerTime
import io.architecture.playground.data.remote.model.NetworkTrace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class EmptyNetworkDataSource @Inject constructor(
) : NetworkDataSource {
    override fun openSession() {}

    override fun closeSession() {}

    override fun streamTraces(): Flow<NetworkTrace> = emptyFlow()

    override fun streamRoutes(): Flow<NetworkRoute> = emptyFlow()

    override fun streamConnectionEvents(): Flow<ConnectionEvent> = emptyFlow()

    override fun sendClientTime(time: NetworkClientTime) {}

    override fun streamServerTime(): Flow<NetworkServerTime> = emptyFlow()

}