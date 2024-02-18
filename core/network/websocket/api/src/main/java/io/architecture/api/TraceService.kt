package io.architecture.api

import io.architecture.api.model.NetworkTrace
import kotlinx.coroutines.flow.Flow

interface TraceService {
    fun streamTraces(): Flow<NetworkTrace>
}