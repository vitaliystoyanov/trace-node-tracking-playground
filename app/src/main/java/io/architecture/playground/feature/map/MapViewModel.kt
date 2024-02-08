package io.architecture.playground.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.data.repository.interfaces.TraceRepository
import io.architecture.playground.data.repository.interfaces.RouteRepository
import io.architecture.playground.domain.ObserveChunkedNodesUseCase
import io.architecture.playground.model.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val nodeRepository: TraceRepository,
    private val routeRepository: RouteRepository,
    observeNodes: ObserveChunkedNodesUseCase,
) : ViewModel() {

    private var _displayRoute = MutableStateFlow<Route?>(null)
    private var _connectionState = nodeRepository.observeConnectionState()
    private var _nodeCounter = nodeRepository.observeCount()


    val uiState: StateFlow<MapUiState> =
        combine(_connectionState, _displayRoute) { connection, displayRoute ->
            MapUiState(displayRoute, connection)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MapUiState(
                null,
                ConnectionState(SocketConnectionState.UNDEFINED)
            )
        )

    val nodesUiState: StateFlow<MapNodesUiState> =
        combine(
            observeNodes(),
            _nodeCounter
        ) { nodes, countNodes ->
            MapNodesUiState(nodes, countNodes)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MapNodesUiState(
                emptySet(),
                0
            )
        )

    fun loadRoute(nodeId: String) = viewModelScope.launch {
        _displayRoute.update { routeRepository.getRouteBy(nodeId) }
    }
}