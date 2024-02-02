package io.architecture.playground.data.remote

import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.remote.websocket.WebSocketTraceService
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultNetworkTraceDataSource @Inject constructor(
    private val webSocketDiverService: WebSocketTraceService
) : NetworkTraceDataSource {

    override fun observeConnection() = webSocketDiverService.observeConnection().consumeAsFlow()
        .map { it.toExternal() }

    override fun streamTraces() = webSocketDiverService.observeTraces().consumeAsFlow()

}