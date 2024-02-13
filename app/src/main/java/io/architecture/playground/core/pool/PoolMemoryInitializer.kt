package io.architecture.playground.core.pool

import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.local.model.TraceEntity
import io.architecture.playground.model.Node
import io.architecture.playground.model.Trace
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PoolMemoryInitializer @Inject constructor() {

    fun initializeWith(
        poolNodes: PoolObjects<Node>,
        poolTraces: PoolObjects<Trace>,
        poolNodeEntities: PoolObjects<NodeEntity>,
        poolTraceEntities: PoolObjects<TraceEntity>
    ) {
        repeat(100_000) {
            poolNodes.release(Node("", 0))
            poolTraces.release(Trace("", 0.0, 0.0, 1, 0.0, 0.0, Date()))
            poolTraceEntities.release(TraceEntity( "", 0.0, 0.0, 1, 0.0, 0.0, Date()))
            poolNodeEntities.release(NodeEntity("", 0))
        }
    }
}