package io.architecture.playground.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.architecture.playground.core.pool.PoolManager
import io.architecture.playground.data.repository.interfaces.ConnectionStateRepository
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.data.repository.interfaces.RouteRepository
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.domain.GetAllNodeWithTraceUseCase
import io.architecture.playground.model.Connection
import io.architecture.playground.model.Node
import io.architecture.playground.model.Route
import io.architecture.playground.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NodesUiState(
    val nodeTraceMap: Map<Node, Trace>,
    val nodeCount: Int,
)

data class RouteUiState(
    val displayRoute: Route?,
)

data class ConnectionUiState(
    val all: Map<Int, Connection>,
)


@HiltViewModel
class MapViewModel @Inject constructor(
    nodeRepository: NodeRepository,
    private val routeRepository: RouteRepository,
    getAllNodeWithTrace: GetAllNodeWithTraceUseCase,
    connectionStateRepository: ConnectionStateRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val poolManager: PoolManager
) : ViewModel() {

    private val _displayRoute = MutableStateFlow<Route?>(null)
    private val _nodeCounter = nodeRepository.observeCount()
    private val _mapNodeTrace = MutableStateFlow<Map<Node, Trace>?>(null)


    val connectionState = connectionStateRepository.connectionStateShared
        .map { ConnectionUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ConnectionUiState(emptyMap())
        )

    val uiState: StateFlow<RouteUiState> =
        _displayRoute
            .map { RouteUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = RouteUiState(
                    null
                )
            )

    val nodesUiState: StateFlow<NodesUiState> =
        combine(
            getAllNodeWithTrace(viewModelScope),
            _nodeCounter
        ) { map, countNodes ->
            _mapNodeTrace.update { map }
            NodesUiState(map, countNodes)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NodesUiState(
                emptyMap(),
                0,
            )
        )

    fun loadRoute(nodeId: String) = viewModelScope.launch(defaultDispatcher) {
        _displayRoute.update { routeRepository.getRouteBy(nodeId) }
    }

    fun releaseRenderedObjects() = viewModelScope.launch(defaultDispatcher) {
        _mapNodeTrace.value?.forEach { (node, trace) ->
            poolManager.getPoolBy(Node::class).release(node)
            poolManager.getPoolBy(Trace::class).release(trace)
        }
    }
}