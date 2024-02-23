package io.architecture.domain

import io.architecture.data.repository.interfaces.TraceRepository
import io.architecture.model.Trace
import io.architecture.runtime.logging.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

class GetStreamTraceByIdUseCase(
    private var traceRepository: TraceRepository,
    private val formatDate: FormatDatetimeUseCase,
    private val convertAzimuthToDirection: ConvertAzimuthToDirectionUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) {

    operator fun invoke(nodeId: String): Flow<Trace> = traceRepository
        .streamTracesBy(nodeId)
        .flowOn(ioDispatcher)
        .onEach { trace ->
            trace.formattedDatetime = formatDate(trace.sentAtTime)
            trace.direction = convertAzimuthToDirection(trace.azimuth)
        }
        .flowOn(defaultDispatcher)
        .catch { error -> Logger.error("REPOSITORY_DEBUG", "GetStreamTraceByIdUseCase: ", error) }
}