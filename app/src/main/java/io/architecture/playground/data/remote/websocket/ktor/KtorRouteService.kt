package io.architecture.playground.data.remote.websocket.ktor

import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.data.remote.websocket.ConnectionEventStreamer
import io.architecture.playground.data.remote.websocket.RouteService
import io.architecture.playground.data.remote.websocket.ktor.internal.KtorProtobufClient
import io.architecture.playground.di.ApplicationScope
import io.architecture.playground.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KtorRouteService @Inject constructor(
    private val client: KtorProtobufClient<Any, NetworkRoute>,
    @ApplicationScope private val scope: CoroutineScope,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : RouteService, ConnectionEventStreamer {

    override fun streamRoutes(): Flow<NetworkRoute> {
        client.openSession()
        return client.receiveShared
    }

    override fun streamConnectionEvents(): Flow<ConnectionEvent> = client.connectionEventsShared

}