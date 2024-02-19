package io.architecture.network.websocket.imp.ktor

import io.architecture.common.ApplicationScope
import io.architecture.common.DefaultDispatcher
import io.architecture.model.ConnectionEvent
import io.architecture.network.websocket.api.ConnectionEventStreamer
import io.architecture.network.websocket.api.TraceService
import io.architecture.network.websocket.api.model.NetworkTrace
import io.architecture.network.websocket.imp.ktor.internal.KtorProtobufClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KtorTraceService @Inject constructor(
    private val client: KtorProtobufClient<Any, NetworkTrace>,
    @ApplicationScope private val scope: CoroutineScope,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : TraceService, ConnectionEventStreamer {

    override fun streamTraces(): Flow<NetworkTrace> {
        client.openSession()
        return client.receiveShared
    }

    override fun streamConnectionEvents(): Flow<ConnectionEvent> = client.connectionEventsShared
}