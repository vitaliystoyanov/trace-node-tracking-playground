package io.architecture.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.data.repository.interfaces.RouteRepository
import io.architecture.domain.GetConnectionStateUseCase
import io.architecture.domain.GetStreamChunkedNodeWithTraceUseCase
import io.architecture.domain.GetStreamTraceUseCase
import io.architecture.model.Route
import io.architecture.model.Trace
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
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = io.architecture.model.Connection(
                rtt = io.architecture.model.UpstreamRtt(
                    0L
                ), isConnected = false
            )
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
        getChunkedNodeWithTrace(isDataBaseStream = false, interval = 1.seconds)
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