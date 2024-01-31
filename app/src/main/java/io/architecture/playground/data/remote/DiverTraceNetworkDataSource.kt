package io.architecture.playground.data.remote

import io.architecture.playground.data.remote.websocket.WebSocketDiverService
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject

class DiverTraceNetworkDataSource @Inject constructor(
    private val webSocketDiverService: WebSocketDiverService
) : NetworkDataSource {

    override fun streamDiverTraces() = webSocketDiverService.observeDiverTraces().consumeAsFlow()
}