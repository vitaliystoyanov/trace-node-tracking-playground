package io.architecture.playground.data.remote.websocket.ktor

import android.util.Log
import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.remote.model.NetworkClientTime
import io.architecture.playground.data.remote.model.NetworkServerTime
import io.architecture.playground.data.remote.websocket.ConnectionEventStreamer
import io.architecture.playground.data.remote.websocket.RttService
import io.architecture.playground.data.remote.websocket.ktor.internal.KtorProtobufClient
import io.architecture.playground.di.ApplicationScope
import io.architecture.playground.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class KtorRttService @Inject constructor(
    val client: KtorProtobufClient<NetworkClientTime, NetworkServerTime>,
    @ApplicationScope private val scope: CoroutineScope,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : RttService, ConnectionEventStreamer {

    override fun sendClientTime(time: NetworkClientTime) {
        client.openSession() // First open
        client.sendShared.tryEmit(time)
    }

    override fun streamServerTime(): Flow<NetworkServerTime> = client.receiveShared


    override fun streamConnectionEvents(): Flow<ConnectionEvent> = client.connectionEventsShared

}