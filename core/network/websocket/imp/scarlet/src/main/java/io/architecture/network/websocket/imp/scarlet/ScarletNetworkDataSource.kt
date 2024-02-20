package io.architecture.network.websocket.imp.scarlet

import androidx.compose.runtime.collectAsState
import io.architecture.datasource.api.NetworkDataSource
import io.architecture.model.ConnectionEvent
import io.architecture.network.websocket.api.model.NetworkRoute
import io.architecture.network.websocket.api.model.NetworkTrace
import io.architecture.network.websocket.api.model.NetworkClientTime
import io.architecture.network.websocket.api.model.NetworkServerTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ScarletNetworkDataSource @Inject constructor(
    private val traceService: ScarletTraceService,
    private val routeService: ScarletRouteService
) : NetworkDataSource {
    override fun openSession() {}

    override fun closeSession() {}

    override fun streamTraces(): Flow<NetworkTrace> = traceService.streamTraces()

    override fun streamRoutes(): Flow<NetworkRoute> = routeService.streamRoutes()

    override fun sendClientTime(time: NetworkClientTime) = traceService.sendClientTime(time)

    override fun streamServerTime(): Flow<NetworkServerTime> = traceService.streamServerTime()

    override fun streamConnectionEvents(): StateFlow<ConnectionEvent> {
        TODO("Not yet implemented")
    }

//    override fun streamConnectionEvents(): StateFlow<ConnectionEvent>
//        traceService.streamConnection().map { ConnectionEvent.valueOf("OPENED") } // TODO !!!
}
