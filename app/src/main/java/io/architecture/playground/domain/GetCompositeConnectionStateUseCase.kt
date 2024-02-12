package io.architecture.playground.domain

import io.architecture.playground.data.repository.DefaultConnectionStateRepository
import io.architecture.playground.model.Connection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class GetCompositeConnectionStateUseCase @Inject constructor(
    private var repository: DefaultConnectionStateRepository
) {

    operator fun invoke(): Flow<Map<Int, Connection>> = combine(
        repository.streamRoundTripTime(50.milliseconds),
        repository.streamTraceConnectionEvents(),
        repository.streamRouteConnectionEvents()
    ) { rtt, trace, route ->
        mapOf(
            Connection.TRACE_SERVICE_CONNECTION to Connection(trace, rtt),
            Connection.ROUTE_SERVICE_CONNECTION to Connection(route, rtt)
        )
    }
}