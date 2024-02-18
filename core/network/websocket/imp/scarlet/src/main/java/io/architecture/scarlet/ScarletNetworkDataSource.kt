package io.architecture.scarlet

import io.architecture.api.model.NetworkClientTime
import io.architecture.api.model.NetworkRoute
import io.architecture.api.model.NetworkServerTime
import io.architecture.api.model.NetworkTrace
import io.architecture.datasource.api.NetworkDataSource
import io.architecture.model.ConnectionEvent
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
        traceService.streamConnection().map {ConnectionEvent.valueOf("OPENED") } // TODO !!!
}
