package io.architecture.data.repository.interfaces

import io.architecture.model.Connection
import io.architecture.model.ConnectionEvent
import io.architecture.model.UpstreamRtt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface ConnectionStateRepository {

    fun streamRoundTripTime(interval: Duration = 1.seconds): Flow<UpstreamRtt>

    fun streamConnectionEvents(): SharedFlow<ConnectionEvent>

}