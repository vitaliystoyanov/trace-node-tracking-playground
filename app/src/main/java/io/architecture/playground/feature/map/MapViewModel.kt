package io.architecture.playground.feature.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.data.repository.interfaces.RouteRepository
import io.architecture.playground.domain.ObserveChunkedNodesUseCase
import io.architecture.playground.model.Route
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val nodeRepository: NodeRepository,
    private val routeRepository: RouteRepository,
    observeNodes: ObserveChunkedNodesUseCase,
) : ViewModel() {

    private var displayRoute: Route? by mutableStateOf(null)
    private var connectionState = nodeRepository.observeConnectionState()
    private var countNodes = nodeRepository.observeNodesCount()


    val uiState: StateFlow<MapUiState> =
        combine(connectionState, snapshotFlow { displayRoute }) { connection, displayRoute ->
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
            countNodes
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

    fun onLoadNodeRoute(nodeId: String) = viewModelScope.launch {
        displayRoute = routeRepository.getRouteBy(nodeId)
    }
}