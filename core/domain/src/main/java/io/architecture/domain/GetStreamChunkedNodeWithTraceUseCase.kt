package io.architecture.domain

import io.architecture.common.ext.chunked
import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.data.repository.interfaces.TraceRepository
import io.architecture.model.Trace
import io.architecture.runtime.logging.Logger
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class GetStreamChunkedNodeWithTraceUseCase(
    private var nodeRepository: NodeRepository,
    private var traceRepository: TraceRepository,
    private val formatDate: FormatDatetimeUseCase,
    private val convertAzimuthToDirection: ConvertAzimuthToDirectionUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) {

    private val defaultAreEquivalentCoordinates: (old: Trace, new: Trace) -> Boolean =
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
        isDatabaseOutgoingStream: Boolean,
        interval: Duration = 1.seconds,
    ): Flow<Sequence<Trace>> {
        require(interval > 0.milliseconds) { "'interval' must be positive: $interval" }

        fun streamTracesViaNetwork() = traceRepository.streamTraces(isPersisted = true)
            .onEach { trace ->
                trace.formattedDatetime = formatDate(trace.sentAtTime)
                trace.direction = convertAzimuthToDirection(trace.azimuth)
            }

        fun streamTracesLocally(id: String): Flow<Trace> = traceRepository
            .streamTracesBy(id)
            .flowOn(ioDispatcher)
            .distinctUntilChanged(defaultAreEquivalentCoordinates)
            .onEach { trace ->
                trace.formattedDatetime = formatDate(trace.sentAtTime)
                trace.direction = convertAzimuthToDirection(trace.azimuth)
            }
            .flowOn(defaultDispatcher)
            .catch { error -> Logger.error("REPOSITORY_DEBUG", "streamTracesLocally: ", error) }

        val downstreamFlow = channelFlow {
            val activeNodesIds = LinkedHashSet<String>()

            nodeRepository.streamNodes()
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
                .catch { error -> Logger.error("REPOSITORY_DEBUG", "streamNodes: ", error) }
                .collect { node -> activeNodesIds.add(node.id) }
        }
        return if (isDatabaseOutgoingStream) downstreamFlow
            .chunked(20_000, interval)
            .cached()
        else streamTracesViaNetwork()
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