package io.architecture.network.websocket.imp.ktor

import io.architecture.model.ConnectionEvent
import io.architecture.network.websocket.api.ConnectionEventStreamer
import io.architecture.network.websocket.api.RouteService
import io.architecture.network.websocket.api.model.NetworkRoute
import io.architecture.network.websocket.imp.ktor.internal.KtorProtobufClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.annotation.Named

class KtorRouteService(
    private val client: KtorProtobufClient<Any, NetworkRoute>,
    @Named("applicationScope") private val scope: CoroutineScope,
    @Named("defaultDispatcher") private val dispatcher: CoroutineDispatcher,
) : RouteService, ConnectionEventStreamer {

    override fun streamRoutes(): Flow<NetworkRoute> {
        client.openSession()
        return client.receiveShared
    }

    override fun streamConnectionEvents(): SharedFlow<ConnectionEvent> =
        client.connectionEventsShared
}