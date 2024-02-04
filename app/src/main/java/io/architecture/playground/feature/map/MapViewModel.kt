package io.architecture.playground.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.architecture.playground.data.NodeRepository
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.model.Node
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MapNodesUiState(
    var latestNodes: List<Node>,
    var latestNodeTraces: Map<String, List<Node>>,
    var nodeCount: Long,
    var connectionState: ConnectionState
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val traceRepository: NodeRepository
) : ViewModel() {

    private var connectionState = traceRepository.getStreamConnectionState()
    private var countNodes = traceRepository.getStreamCountNodes()
    private var latestTracesByNodeIds = traceRepository.getStreamLatestNodes()

    val uiState: StateFlow<MapNodesUiState> =
        combine(latestTracesByNodeIds, connectionState, countNodes) { traces, connection, count ->
            val map = mutableMapOf<String, List<Node>>()

            traces.forEach {
                map[it.nodeId] = traceRepository.getAllTracesBy(it.nodeId)
            }

            MapNodesUiState(traces, map, count, connection)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MapNodesUiState(
                    emptyList(),
                    emptyMap(),
                    0,
                    ConnectionState(SocketConnectionState.UNDEFINED)
                )
            )
}