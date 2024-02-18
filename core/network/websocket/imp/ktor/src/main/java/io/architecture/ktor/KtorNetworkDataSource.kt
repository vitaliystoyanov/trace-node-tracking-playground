package io.architecture.ktor

import io.architecture.api.model.NetworkClientTime
import io.architecture.api.model.NetworkRoute
import io.architecture.api.model.NetworkServerTime
import io.architecture.api.model.NetworkTrace
import io.architecture.datasource.api.NetworkDataSource
import io.architecture.ktor.KtorRouteService
import io.architecture.ktor.KtorRttService
import io.architecture.ktor.KtorTraceService
import io.architecture.model.ConnectionEvent
import kotlinx.coroutines.flow.Flow
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

    override fun streamConnectionEvents(): Flow<ConnectionEvent> =
        traceService.streamConnectionEvents() // Only trace, later will add other websocket connection states
//            .filter { it != ConnectionEvent.MESSAGE_RECEIVED }
}