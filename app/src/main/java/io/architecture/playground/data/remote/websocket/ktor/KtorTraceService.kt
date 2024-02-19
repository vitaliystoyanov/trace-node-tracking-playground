package io.architecture.playground.data.remote.websocket.ktor

import android.util.Log
import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.remote.model.NetworkTrace
import io.architecture.playground.data.remote.websocket.ConnectionEventStreamer
import io.architecture.playground.data.remote.websocket.TraceService
import io.architecture.playground.data.remote.websocket.ktor.internal.KtorProtobufClient
import io.architecture.playground.di.ApplicationScope
import io.architecture.playground.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
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