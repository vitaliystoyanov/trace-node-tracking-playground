package io.architecture.network.websocket.imp.ktor

import io.architecture.datasource.api.NetworkDataSource
import io.architecture.model.ConnectionEvent
import io.architecture.network.websocket.api.model.NetworkClientTime
import io.architecture.network.websocket.api.model.NetworkRoute
import io.architecture.network.websocket.api.model.NetworkServerTime
import io.architecture.network.websocket.api.model.NetworkTrace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class KtorNetworkDataSource @Inject constructor(
    private val traceService: KtorTraceService,
    private val routeService: KtorRouteService,
    private val rttService: KtorRttService,
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

    override fun sendClientTime(time: NetworkClientTime) {
        rttService.sendClientTime(time)
    }

    override fun streamServerTime(): Flow<NetworkServerTime> {
        return rttService.streamServerTime()
    }

    override fun streamConnectionEvents(): SharedFlow<ConnectionEvent> =
        traceService.streamConnectionEvents() // Only trace, later will add other websocket connection states
}