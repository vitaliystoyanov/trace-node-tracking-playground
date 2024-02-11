package io.architecture.playground.domain

import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.Node
import io.architecture.playground.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllNodeWithTraceUseCase @Inject constructor(
    private var nodeRepository: NodeRepository,
    private val formatDate: FormatDatetimeUseCase,
    private val convertAzimuthToDirection: ConvertAzimuthToDirectionUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    operator fun invoke(scope: CoroutineScope): Flow<Map<Node, Trace>> = flow {
        while (true) {
            emit(scope.async {
                nodeRepository.getNodesWithLastTrace()
                    .onEach {
                        it.value.formattedDatetime = formatDate(it.value.time)
                        it.value.direction = convertAzimuthToDirection(it.value.azimuth)
                    }
            }.await())
            delay(1000)
        }
    }
}