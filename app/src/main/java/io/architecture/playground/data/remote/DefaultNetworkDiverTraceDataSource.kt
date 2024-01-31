package io.architecture.playground.data.remote

import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.remote.websocket.WebSocketDiverService
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultNetworkDiverTraceDataSource @Inject constructor(
    private val webSocketDiverService: WebSocketDiverService
) : NetworkDiverTraceDataSource {

    override fun observeConnection() = webSocketDiverService.observeConnection().consumeAsFlow()
        .map { it.toExternal() }

    override fun streamDiverTraces() = webSocketDiverService.observeDiverTraces().consumeAsFlow()

}