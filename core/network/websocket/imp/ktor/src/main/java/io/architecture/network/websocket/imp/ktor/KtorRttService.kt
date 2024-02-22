package io.architecture.network.websocket.imp.ktor

import io.architecture.model.ConnectionEvent
import io.architecture.network.websocket.api.ConnectionEventStreamer
import io.architecture.network.websocket.api.RttService
import io.architecture.network.websocket.api.model.NetworkClientTime
import io.architecture.network.websocket.api.model.NetworkServerTime
import io.architecture.network.websocket.imp.ktor.internal.KtorProtobufClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class KtorRttService(
    val client: KtorProtobufClient<NetworkClientTime, NetworkServerTime>,
    @Named("applicationScope") private val scope: CoroutineScope,
    @Named("defaultDispatcher") private val dispatcher: CoroutineDispatcher,
) : RttService, ConnectionEventStreamer {


    init {
        client.openSession() // First open
    }

    override suspend fun sendClientTime(time: NetworkClientTime) {
        client.sendShared.emit(time)
    }

    override fun streamServerTime(): Flow<NetworkServerTime> = client.receiveShared

    override fun streamConnectionEvents(): SharedFlow<ConnectionEvent> =
        client.connectionEventsShared
}