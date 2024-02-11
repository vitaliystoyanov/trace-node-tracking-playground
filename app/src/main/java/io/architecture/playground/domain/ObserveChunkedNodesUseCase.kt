package io.architecture.playground.domain

import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.model.CompositeNodeTrace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ObserveChunkedNodesUseCase @Inject constructor(
    private var nodeRepository: NodeRepository,
    private val formatDate: FormatDatetimeUseCase,
    private val convertAzimuthToDirection: ConvertAzimuthToDirectionUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<CompositeNodeTrace> =
        nodeRepository.observeNodesWithLastTrace()
            .flatMapConcat { it.asFlow() }
            .distinctUntilChangedBy { it.node.id }
            .onEach {
                it.trace.formattedDatetime = formatDate(it.trace.time)
                it.trace.direction = convertAzimuthToDirection(it.trace.azimuth)
            }
            .flowOn(defaultDispatcher)
}