package io.architecture.ktor

import io.architecture.api.ConnectionEventStreamer
import io.architecture.api.RttService
import io.architecture.api.model.NetworkClientTime
import io.architecture.api.model.NetworkServerTime
import io.architecture.common.ApplicationScope
import io.architecture.common.DefaultDispatcher
import io.architecture.ktor.internal.KtorProtobufClient
import io.architecture.model.ConnectionEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
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