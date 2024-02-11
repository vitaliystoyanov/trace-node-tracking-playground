package io.architecture.playground.data.repository

import io.architecture.playground.core.pool.PoolManager
import io.architecture.playground.data.local.LocalDataSource
import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.mapping.assignProperties
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.CompositeNodeTrace
import io.architecture.playground.model.Node
import io.architecture.playground.model.NodeMode
import io.architecture.playground.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultNodeRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    poolManager: PoolManager,
) : NodeRepository {

    private val poolNodeEntities = poolManager.getPoolByMember<NodeEntity>()
    private val poolNodes = poolManager.getPoolByMember<Node>()
    private val poolTraces = poolManager.getPoolByMember<Trace>()

    override suspend fun updateOrAdd(node: Node): Unit = withContext(ioDispatcher) {
        val nodeEntityPooled = poolNodeEntities.acquire()!!
        localDataSource.updateOrCreate(assignProperties(nodeEntityPooled, node))
        poolNodeEntities.release(nodeEntityPooled)
    }

    override fun observeNodesWithLastTrace(): Flow<Sequence<CompositeNodeTrace>> =
        localDataSource.observeAllNodesWithLatestTrace()
            .map { it.toExternal() }
            .flowOn(defaultDispatcher)

    override suspend fun getNodesWithLastTrace(): Map<Node, Trace> =
        withContext(ioDispatcher) {
            localDataSource.getAllNodesWithLatestTrace().associate {
                val nodePooled = poolNodes.acquire()!!
                val tracePooled = poolTraces.acquire()!!
                nodePooled.apply {
                    id = it.node.id
                    mode = NodeMode.valueOf(it.node.mode)
                    lastTraceTimestamp = it.trace.timestamp.time
                }
                tracePooled.apply {
                    id = it.trace.id
                    nodeId = it.trace.nodeId
                    lon = it.trace.lon
                    lat = it.trace.lat
                    speed = it.trace.speed
                    azimuth = it.trace.azimuth
                    alt = it.trace.alt
                    time = it.trace.timestamp
                }

                nodePooled to tracePooled
            }
        }

    override fun observeCount(): Flow<Int> = localDataSource.observeNodeCount()
}