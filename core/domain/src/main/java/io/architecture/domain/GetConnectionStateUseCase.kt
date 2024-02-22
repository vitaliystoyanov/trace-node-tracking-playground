package io.architecture.domain

import android.util.Log
import io.architecture.data.repository.interfaces.ConnectionStateRepository
import io.architecture.data.repository.toExternal
import io.architecture.model.Connection
import io.architecture.model.UpstreamRtt
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlin.time.Duration.Companion.milliseconds

class GetConnectionStateUseCase(
    private var stateRepository: ConnectionStateRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) {
    operator fun invoke(): Flow<Connection> =
        stateRepository.streamConnectionEvents()
            .filterNot { it.toExternal().state == Connection.State.MESSAGE_RECEIVED }
            .map { it.toExternal() }
            .combine(
                stateRepository.streamRoundTripTime(interval = 500.milliseconds)
                    .onStart { emit(UpstreamRtt(0)) } // To kick off combine if server doesn't response
            ) { connection: Connection, rtt: UpstreamRtt ->
                connection.rtt = rtt
                connection
            }
            .onEach { Log.d("RTT_NETWORK", "GetConnectionStateUseCase: Connection -> $it") }

}