package io.architecture.playground.data.remote

import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.remote.interfaces.NetworkDataSource
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.data.remote.websocket.RoutesService
import io.architecture.playground.data.remote.websocket.TraceService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultNetworkDataSource @Inject constructor(
    private val traceService: TraceService,
    private val routeService: RoutesService
) : NetworkDataSource {

    override fun streamConnection() = traceService.streamConnection().consumeAsFlow()
        .map { it.toExternal() }

    override fun streamTraces() = traceService.streamTraces().consumeAsFlow()

    override fun streamRoutes(): Flow<NetworkRoute> = routeService.streamRoutes().consumeAsFlow()
}