package io.architecture.playground.data.remote

import io.architecture.playground.data.remote.model.NetworkTrace
import kotlinx.coroutines.flow.Flow

interface NetworkTraceDataSource : StreamableConnectionDataSource {

    fun streamTraces(): Flow<NetworkTrace>

}