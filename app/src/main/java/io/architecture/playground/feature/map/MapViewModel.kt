package io.architecture.playground.feature.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.architecture.playground.data.repository.interfaces.ConnectionStateRepository
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.data.repository.interfaces.RouteRepository
import io.architecture.playground.domain.GetStreamChunkedNodeWithTraceUseCase
import io.architecture.playground.domain.GetStreamTraceUseCase
import io.architecture.playground.model.Route
import io.architecture.playground.model.Trace
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

data class DetailsUiState(val route: Route?, val lastTrace: Trace?)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val routeRepository: RouteRepository,
    nodeRepository: NodeRepository,
    getChunkedNodeWithTrace: GetStreamChunkedNodeWithTraceUseCase,
    private val getStreamTrace: GetStreamTraceUseCase,
    connectionStateRepository: ConnectionStateRepository,
) : ViewModel() {

    private lateinit var job: Job
    private val _displayDetails = MutableStateFlow(DetailsUiState(null, null))

    val connectionsUiState = connectionStateRepository.connectionStateShared
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
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
                    "REPOSITORY_DEBUG",
                    "chunked list size of traces to sequence - ${it.count()}"
                )
            }
            .map { it.asSequence() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySequence()
            )

    fun clearDetails(nodeId: String) {
        if (job!= null && job.isActive) job.cancel()
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