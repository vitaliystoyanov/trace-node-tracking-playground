package io.architecture.playground.data.remote

import io.architecture.playground.data.mapping.toExternalAs
import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.remote.model.NetworkClientTime
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.data.remote.model.NetworkServerTime
import io.architecture.playground.data.remote.model.NetworkTrace
import io.architecture.playground.data.remote.websocket.scarlet.ScarletRouteService
import io.architecture.playground.data.remote.websocket.scarlet.ScarletTraceService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScarletNetworkDataSource @Inject constructor(
    private val traceService: ScarletTraceService,
    private val routeService: ScarletRouteService
) : NetworkDataSource {
    override fun openSession() {}

    override fun closeSession() {}

    override fun streamTraces(): Flow<NetworkTrace> = traceService.streamTraces()

    override fun streamRoutes(): Flow<NetworkRoute> = routeService.streamRoutes()

    override fun sendClientTime(time: NetworkClientTime) = traceService.sendClientTime(time)

    override fun streamServerTime(): Flow<NetworkServerTime> = traceService.streamServerTime()

    override fun streamConnectionEvents(): Flow<ConnectionEvent> =
        traceService.streamConnection()
            .map { it.toExternalAs() }
}
