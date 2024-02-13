package io.architecture.playground.data.remote

import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.remote.interfaces.NetworkDataSource
import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.remote.model.NetworkClientTime
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.data.remote.model.NetworkServerTime
import io.architecture.playground.data.remote.model.NetworkTrace
import io.architecture.playground.data.remote.websocket.RoutesService
import io.architecture.playground.data.remote.websocket.TraceService
import io.architecture.playground.model.Trace
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class DefaultNetworkDataSource @Inject constructor(
    private val traceService: TraceService,
    private val routeService: RoutesService
) : NetworkDataSource {

    override fun streamTraces(): Flow<Trace> = traceService.streamTraces()
        .receiveAsFlow()
        .map { it.toExternal() }

    override fun streamTracesAsChannel(): ReceiveChannel<NetworkTrace> =
        traceService.streamTraces()

    override fun streamRoutes(): Flow<NetworkRoute> = routeService.streamRoutes().receiveAsFlow()

    override fun streamRouteConnectionState(): Flow<ConnectionEvent> =
        routeService.streamConnection().receiveAsFlow()
            .map { it.toExternal() }

    override fun sendClientTime(time: NetworkClientTime) =
        traceService.sendClientTime(time)


    override fun streamServerTime(): Flow<NetworkServerTime> =
        traceService.streamServerTime().receiveAsFlow()

    override fun streamTraceConnectionState(): Flow<ConnectionEvent> =
        traceService.streamConnection().receiveAsFlow()
            .map { it.toExternal() }
}
