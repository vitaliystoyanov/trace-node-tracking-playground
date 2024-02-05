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
import io.architecture.playground.domain.ConvertAzimuthToDirectionUseCase
import io.architecture.playground.domain.FormatDateUseCase
import io.architecture.playground.model.Node
import io.architecture.playground.model.Route
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapNodesUiState(
    var nodes: List<Node>,
    var nodeCount: Long,
    var displayRoute: Route?,
    var connectionState: ConnectionState
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val nodeRepository: NodeRepository,
    private val routeRepository: RouteRepository,
    formatDate: FormatDateUseCase,
    convertAzimuthToDirection: ConvertAzimuthToDirectionUseCase,
) : ViewModel() {

    private var displayRoute: Route? by mutableStateOf(null)
    private var latestNodes = nodeRepository.observeLatestNodes()
    private var connectionState = nodeRepository.observeConnectionState()
    private var countNodes = nodeRepository.observeNodesCount()

    val uiState: StateFlow<MapNodesUiState> =
        combine(
            latestNodes,
            connectionState,
            snapshotFlow { displayRoute },
            countNodes
        ) { nodes, connection, displayRoute, count ->
            nodes.forEach { node ->
                node.formattedDatetime = formatDate(node.time)
                node.direction = convertAzimuthToDirection(node.azimuth)
            }
            MapNodesUiState(nodes, count, displayRoute, connection)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MapNodesUiState(
                    emptyList(),
                    0,
                    null,
                    ConnectionState(SocketConnectionState.UNDEFINED)
                )
            )

    fun onLoadNodeRoute(nodeId: String) = viewModelScope.launch {
        displayRoute = routeRepository.getRouteBy(nodeId)
    }
}