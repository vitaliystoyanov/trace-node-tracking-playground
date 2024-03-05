package io.architecture.map

import androidx.lifecycle.ViewModel
import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.data.repository.interfaces.RouteRepository
import io.architecture.domain.GetConnectionStateUseCase
import io.architecture.domain.GetStreamChunkedNodeWithTraceUseCase
import io.architecture.domain.GetStreamTraceByIdUseCase


open class MapViewModel(
    private val getStreamTrace: GetStreamTraceByIdUseCase,
    private val routeRepository: RouteRepository, // TODO Extract to use case
    getChunkedNodeWithTrace: GetStreamChunkedNodeWithTraceUseCase,
    connectionState: GetConnectionStateUseCase,
    nodeRepository: NodeRepository,  // TODO Extract to use case
) : ViewModel() {
    val composite = MapViewModel(
        getStreamTrace,
        routeRepository,
        getChunkedNodeWithTrace,
        connectionState,
        nodeRepository
    )
}