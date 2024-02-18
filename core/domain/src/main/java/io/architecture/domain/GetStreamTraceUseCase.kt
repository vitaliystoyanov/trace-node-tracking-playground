package io.architecture.domain

import android.util.Log
import io.architecture.common.DefaultDispatcher
import io.architecture.common.IoDispatcher
import io.architecture.data.mapping.toExternalAs
import io.architecture.data.repository.interfaces.TraceRepository
import io.architecture.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GetStreamTraceUseCase @Inject constructor(
    private var traceRepository: TraceRepository,
    private val formatDate: FormatDatetimeUseCase,
    private val convertAzimuthToDirection: ConvertAzimuthToDirectionUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
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