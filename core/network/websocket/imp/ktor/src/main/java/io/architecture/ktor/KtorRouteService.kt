package io.architecture.ktor

import io.architecture.api.ConnectionEventStreamer
import io.architecture.api.RouteService
import io.architecture.api.model.NetworkRoute
import io.architecture.common.ApplicationScope
import io.architecture.common.DefaultDispatcher
import io.architecture.ktor.internal.KtorProtobufClient
import io.architecture.model.ConnectionEvent
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

    override fun streamConnectionEvents(): Flow<ConnectionEvent> =
        client.connectionEventsShared

}