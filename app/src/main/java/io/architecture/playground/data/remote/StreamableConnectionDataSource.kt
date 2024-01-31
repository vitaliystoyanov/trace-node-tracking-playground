package io.architecture.playground.data.remote

import io.architecture.playground.data.remote.model.NetworkConnectionEvent
import kotlinx.coroutines.flow.Flow

interface StreamableConnectionDataSource {

    fun observeConnection(): Flow<NetworkConnectionEvent>

}