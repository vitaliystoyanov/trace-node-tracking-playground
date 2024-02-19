package io.architecture.playground.data.remote

import android.util.Log
import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.remote.model.NetworkClientTime
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.data.remote.model.NetworkServerTime
import io.architecture.playground.data.remote.model.NetworkTrace
import io.architecture.playground.data.remote.websocket.ktor.KtorRouteService
import io.architecture.playground.data.remote.websocket.ktor.KtorRttService
import io.architecture.playground.data.remote.websocket.ktor.KtorTraceService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
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