package io.architecture.playground.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.architecture.playground.data.TraceRepository
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.model.Trace
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MapNodesUiState(
    var latestTraces: List<Trace>,
    var latestTraceRoutes: Map<String, List<Trace>>,
    var tracesCount: Long,
    var connectionState: ConnectionState
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val traceRepository: TraceRepository
) : ViewModel() {

    private var connectionState = traceRepository.getStreamConnectionState()
    private var countTraces = traceRepository.getStreamCountTraces()
    private var latestTracesByNodeIds = traceRepository.getStreamLatestTraceByUniqNodeIds()

    val uiState: StateFlow<MapNodesUiState> =
        combine(latestTracesByNodeIds, connectionState, countTraces) { traces, connection, count ->
            val map = mutableMapOf<String, List<Trace>>()

            traces.forEach {
                map[it.nodeId] = traceRepository.getAllTracesByNodeId(it.nodeId)
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