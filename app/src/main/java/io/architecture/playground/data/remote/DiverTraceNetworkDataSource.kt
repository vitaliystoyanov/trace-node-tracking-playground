package io.architecture.playground.data.remote

import com.tinder.scarlet.WebSocket
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.remote.websocket.WebSocketDiverService
import io.architecture.playground.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class DiverTraceNetworkDataSource @Inject constructor(
    private val webSocketDiverService: WebSocketDiverService
) : NetworkDataSource {

    override fun observeConnection() = webSocketDiverService.observeConnection().consumeAsFlow()
        .map { it.toExternal() }

    override fun streamDiverTraces() = webSocketDiverService.observeDiverTraces().consumeAsFlow()

}