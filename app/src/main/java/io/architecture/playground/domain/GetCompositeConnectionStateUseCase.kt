package io.architecture.playground.domain

import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.repository.DefaultStreamConnectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetCompositeConnectionStateUseCase @Inject constructor(
    private var streamConnectionRepository: DefaultStreamConnectionRepository
) {

    operator fun invoke(): Flow<Map<Int, ConnectionState>> = combine(
        streamConnectionRepository.streamTraceConnectionEvents(),
        streamConnectionRepository.streamRouteConnectionEvents()
    ) { trace, route ->
        mapOf(
            Connection.TRACE_SERVICE_CONNECTION to trace,
            Connection.TRACE_SERVICE_CONNECTION to route
        )
    }
}

class Connection {
    var state: ConnectionState = ConnectionState.UNDEFINED

    fun changeStatus(connectionEvent: ConnectionEvent) {
        // Simplification ;)
        state = connectionEvent
    }

    companion object {
        const val ROUTE_SERVICE_CONNECTION = 0
        const val TRACE_SERVICE_CONNECTION = 1
    }
}

// Simplification ;)
typealias ConnectionState = ConnectionEvent