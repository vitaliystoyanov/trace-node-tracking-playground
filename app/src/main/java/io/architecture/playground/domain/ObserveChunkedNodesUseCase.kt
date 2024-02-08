package io.architecture.playground.domain

import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.ext.chunkedSetBy
import io.architecture.playground.model.Node
import io.architecture.playground.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class ObserveChunkedNodesUseCase @Inject constructor(
    private var nodeRepository: NodeRepository,
    private val formatDate: FormatDatetimeUseCase,
    private val convertAzimuthToDirection: ConvertAzimuthToDirectionUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Set<Pair<Node, Trace>>> =
        nodeRepository.observeNodesWithLastTrace()
            .flatMapConcat { it.asFlow() }
            .onEach {
                it.second.formattedDatetime = formatDate(it.second.time)
                it.second.direction = convertAzimuthToDirection(it.second.azimuth)
            }
            .chunkedSetBy(10_000, 16.milliseconds) { it.first.id } // TODO Dynamic max size
            .flowOn(defaultDispatcher)
}