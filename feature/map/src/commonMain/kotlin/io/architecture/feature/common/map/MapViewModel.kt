package io.architecture.feature.common.map

import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.data.repository.interfaces.RouteRepository
import io.architecture.domain.GetConnectionStateUseCase
import io.architecture.domain.GetStreamChunkedNodeWithTraceUseCase
import io.architecture.domain.GetStreamTraceByIdUseCase
import io.architecture.model.Connection
import io.architecture.model.Route
import io.architecture.model.Trace
import io.architecture.model.UpstreamRtt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

data class NodeDetailsUiState(val route: Route?, val lastTrace: Trace?)

class MapViewModel(
    private val scope: CoroutineScope,
    private val getStreamTrace: GetStreamTraceByIdUseCase,
    private val routeRepository: RouteRepository, // TODO Extract to use case
    getChunkedNodeWithTrace: GetStreamChunkedNodeWithTraceUseCase,
    connectionState: GetConnectionStateUseCase,
    nodeRepository: NodeRepository,  // TODO Extract to use case
) {

    private lateinit var job: Job
    private val _displayDetails = MutableStateFlow(NodeDetailsUiState(null, null))

    val connectionsUiState = connectionState()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Connection(
                rtt = UpstreamRtt(
                    0L
                ), isConnected = false
            )
        )

    val detailsUiState: StateFlow<NodeDetailsUiState> = _displayDetails
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NodeDetailsUiState(null, null)
        )

    val nodeCounterUiState: StateFlow<Int> = nodeRepository.streamCount()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    val tracesUiState: StateFlow<Sequence<Trace>> =
        getChunkedNodeWithTrace(isDatabaseOutgoingStream = false, interval = 1.seconds)
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySequence()
            )

    fun clearDetails() {
        if (job.isActive) job.cancel() // TODO do it without cancel here in compose function
    }

    fun loadDetails(nodeId: String) {
        job = scope.launch {
            val route = routeRepository.getRouteBy(nodeId)
            getStreamTrace(nodeId).collect { lastTrace ->
                _displayDetails.update {
                    NodeDetailsUiState(route, lastTrace)
                }
            }
        }
    }
}