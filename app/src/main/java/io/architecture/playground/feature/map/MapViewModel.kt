package io.architecture.playground.feature.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.data.repository.interfaces.RouteRepository
import io.architecture.playground.domain.GetConnectionStateUseCase
import io.architecture.playground.domain.GetStreamChunkedNodeWithTraceUseCase
import io.architecture.playground.domain.GetStreamTraceUseCase
import io.architecture.playground.model.Connection
import io.architecture.playground.model.Route
import io.architecture.playground.model.Trace
import io.architecture.playground.model.UpstreamRtt
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

data class DetailsUiState(val route: Route?, val lastTrace: Trace?)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getStreamTrace: GetStreamTraceUseCase,
    private val routeRepository: RouteRepository,
    getChunkedNodeWithTrace: GetStreamChunkedNodeWithTraceUseCase,
    connectionState: GetConnectionStateUseCase,
    nodeRepository: NodeRepository,
) : ViewModel() {

    private lateinit var job: Job
    private val _displayDetails = MutableStateFlow(DetailsUiState(null, null))

    val connectionsUiState = connectionState()
        .onEach {
            Log.d(
                "REPOSITORY_DEBUG",
                "VIEW_MODEL -> connectionsUiState -> state - $it"
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Connection(rtt = UpstreamRtt(0L), isConnected = false)
        )

    val detailsUiState: StateFlow<DetailsUiState> = _displayDetails
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailsUiState(null, null)
        )

    val nodeCounterUiState: StateFlow<Int> = nodeRepository.streamCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    val tracesUiState: StateFlow<Sequence<Trace>> =
        getChunkedNodeWithTrace(false, 1.seconds)
            .onEach {
                Log.d(
                    "REPOSITORY_DEBUG_N",
                    "chunked list size of traces to sequence - ${it.count()}"
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySequence()
            )

    fun clearDetails(nodeId: String) {
        if (job.isActive) job.cancel()
    }

    fun loadDetails(nodeId: String) {
        job = viewModelScope.launch {
            val route = routeRepository.getRouteBy(nodeId)
            getStreamTrace(nodeId).collect { lastTrace ->
                _displayDetails.update {
                    DetailsUiState(route, lastTrace)
                }
            }
        }
    }
}