package io.architecture.domain

import android.util.Log
import io.architecture.data.repository.interfaces.TraceRepository
import io.architecture.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

class GetStreamTraceUseCase(
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
        .catch { error -> Log.e("REPOSITORY_DEBUG", "streamTracesLocally: ", error) }
}