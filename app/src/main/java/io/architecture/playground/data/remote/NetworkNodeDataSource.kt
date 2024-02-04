package io.architecture.playground.data.remote

import io.architecture.playground.data.remote.model.NetworkNode
import kotlinx.coroutines.flow.Flow

interface NetworkNodeDataSource : StreamableConnectionDataSource {

    fun streamNodes(): Flow<NetworkNode>

}