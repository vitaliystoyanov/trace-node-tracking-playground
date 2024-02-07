package io.architecture.playground.domain

import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.ext.chunkedSetBy
import io.architecture.playground.model.Node
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class ObserveChunkedNodesUseCase @Inject constructor(
    private var nodesRepository: NodeRepository,
    private val formatDate: FormatDateUseCase,
    private val convertAzimuthToDirection: ConvertAzimuthToDirectionUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Set<Node>> = nodesRepository.observeListNodes()
        .flatMapLatest { it.asFlow() }
        .onEach {
            it.formattedDatetime = formatDate(it.time)
            it.direction = convertAzimuthToDirection(it.azimuth)
        }
        .chunkedSetBy(1000, 16.milliseconds) { it.nodeId }
        .flowOn(defaultDispatcher)
}