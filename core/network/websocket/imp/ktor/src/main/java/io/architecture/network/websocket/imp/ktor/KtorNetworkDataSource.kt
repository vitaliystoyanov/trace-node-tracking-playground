package io.architecture.network.websocket.imp.ktor

import io.architecture.datasource.api.NetworkDataSource
import io.architecture.model.ConnectionEvent
import io.architecture.network.websocket.api.RouteService
import io.architecture.network.websocket.api.RttService
import io.architecture.network.websocket.api.TraceService
import io.architecture.network.websocket.api.model.NetworkClientTime
import io.architecture.network.websocket.api.model.NetworkRoute
import io.architecture.network.websocket.api.model.NetworkServerTime
import io.architecture.network.websocket.api.model.NetworkTrace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

class KtorNetworkDataSource(
    private val traceService: TraceService,
    private val routeService: RouteService,
    private val rttService: RttService,
) : NetworkDataSource {
    override fun openSession() {
    }

    override fun closeSession() {
    }

    override fun streamTraces(): Flow<NetworkTrace> {
        return traceService.streamTraces()
    }

    override fun streamRoutes(): Flow<NetworkRoute> {
        return routeService.streamRoutes()
    }

    override suspend fun sendClientTime(time: NetworkClientTime) {
        rttService.sendClientTime(time)
    }

    override fun streamServerTime(): Flow<NetworkServerTime> {
        return rttService.streamServerTime()
    }

    override fun streamConnectionEvents(): SharedFlow<ConnectionEvent> =
        traceService.streamConnectionEvents() // Only trace, later will add other websocket connection states
}