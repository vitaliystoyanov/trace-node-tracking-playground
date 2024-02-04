package io.architecture.playground.data.remote

import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.remote.websocket.NodeWebSocketService
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultNetworkNodeDataSource @Inject constructor(
    private val webSocketDiverService: NodeWebSocketService
) : NetworkNodeDataSource {

    override fun observeConnection() = webSocketDiverService.observeConnection().consumeAsFlow()
        .map { it.toExternal() }

    override fun streamNodes() = webSocketDiverService.observeNodes().consumeAsFlow()

}