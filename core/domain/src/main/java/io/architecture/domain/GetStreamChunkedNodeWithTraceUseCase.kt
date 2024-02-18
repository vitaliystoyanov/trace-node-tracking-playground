package io.architecture.domain

import io.architecture.common.DefaultDispatcher
import io.architecture.common.IoDispatcher
import io.architecture.common.ext.chunked
import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.data.repository.interfaces.TraceRepository
import io.architecture.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class GetStreamChunkedNodeWithTraceUseCase @Inject constructor(
    private var nodeRepository: NodeRepository,
    private var traceRepository: TraceRepository,
    private val formatDate: FormatDatetimeUseCase,
    private val convertAzimuthToDirection: ConvertAzimuthToDirectionUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

//    private val defaultAreEquivalentCoordinates: (old: TraceEntity, new: TraceEntity) -> Boolean =
//        { old, new -> old.lon == new.lon || old.lat == new.lat }

    /*
    *
    *
    * Note: Observable queries in Room have one important limitation: the query reruns whenever
    * any row in the table is updated, whether or not that row is in the result set.
    * You can ensure that the UI is only notified when the actual query results change
    * by applying the distinctUntilChanged() operator from the corresponding library: Flow
    *
    * Because SQLite database triggers only allow notifications at table level and not at row level,
    * Room can’t know what exactly has changed in the table data,
    * therefore it re-triggers the query defined in the DAO
    *
    *
    * */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        interval: Duration = 1.seconds
    ): Flow<Sequence<Trace>> {
        require(interval > 0.milliseconds) { "'interval' must be positive: $interval" }

        fun streamTracesViaNetwork() = traceRepository.streamViaNetwork()
            .onEach { trace ->
                trace.formattedDatetime = formatDate(trace.sentAtTime)
                trace.direction = convertAzimuthToDirection(trace.azimuth)
            }

        return streamTracesViaNetwork()
            .chunked(20_000, interval)
            .cached()
    }
}

fun Flow<List<Trace>>.cached(): Flow<Sequence<Trace>> =
    flow {
        val cache = LinkedHashMap<String, Trace>()
        collect { list ->
            list.map { cache.put(it.nodeId, it) }
            emit(cache.values.asSequence())
        }
    }