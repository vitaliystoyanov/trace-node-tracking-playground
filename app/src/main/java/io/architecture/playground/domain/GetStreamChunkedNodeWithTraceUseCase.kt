package io.architecture.playground.domain

import android.util.Log
import io.architecture.playground.data.local.model.TraceEntity
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.data.repository.interfaces.TraceRepository
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.ext.chunked
import io.architecture.playground.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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

    private val defaultAreEquivalentCoordinates: (old: TraceEntity, new: TraceEntity) -> Boolean =
        { old, new -> old.lon == new.lon || old.lat == new.lat }

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
        isDataBaseStream: Boolean,
        interval: Duration = 1.seconds
    ): Flow<List<Trace>> {
        require(interval > 0.milliseconds) { "'interval' must be positive: $interval" }

        fun streamTracesViaNetwork() = traceRepository.streamViaNetwork()
            .onEach { trace ->
                trace.formattedDatetime = formatDate(trace.sentAtTime)
                trace.direction = convertAzimuthToDirection(trace.azimuth)
            }

        fun streamTracesLocally(id: String): Flow<Trace> = traceRepository
            .streamTracesBy(id)
            .flowOn(ioDispatcher)
            .distinctUntilChanged(defaultAreEquivalentCoordinates)
            .map { traceEntity -> traceEntity.toExternal() }
            .onEach { trace ->
                trace.formattedDatetime = formatDate(trace.sentAtTime)
                trace.direction = convertAzimuthToDirection(trace.azimuth)
            }
            .flowOn(defaultDispatcher)
            .catch { error -> Log.e("REPOSITORY_DEBUG", "streamTracesLocally: ", error) }

        val downstreamFlow = channelFlow {

            val activeNodesIds = LinkedHashSet<String>()

            nodeRepository.streamAllNodes()
                .flowOn(ioDispatcher)
                .flatMapLatest { nodeList -> nodeList.asFlow() }
                .distinctUntilChangedBy { it.id }
                .filterNot { node -> node.id in activeNodesIds }
                .onEach { node ->
                    launch {
                        streamTracesLocally(node.id).collect { trace ->
                            send(trace)
                        }
                    }
                }
                .flowOn(defaultDispatcher)
                .catch { error -> Log.e("REPOSITORY_DEBUG", "streamAllNodes: ", error) }
                .collect { node -> activeNodesIds.add(node.id) }
        }
        return if (isDataBaseStream) downstreamFlow
            .chunked(20_000, interval)
            .cached()
        else streamTracesViaNetwork()
            .chunked(20_000, interval)
            .cached()
    }
}

fun Flow<List<Trace>>.cached(
): Flow<List<Trace>> = flow {
    val cache = LinkedHashMap<String, Trace>()
    collect { list ->
        list.map { cache.put(it.nodeId, it) }
        emit(cache.values.toList())
    }
}