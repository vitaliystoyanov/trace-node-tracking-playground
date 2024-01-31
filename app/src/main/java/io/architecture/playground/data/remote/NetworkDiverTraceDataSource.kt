package io.architecture.playground.data.remote

import io.architecture.playground.data.remote.model.NetworkDiverTrace
import kotlinx.coroutines.flow.Flow

interface NetworkDiverTraceDataSource : StreamableConnectionDataSource {

    fun streamDiverTraces(): Flow<NetworkDiverTrace>

}