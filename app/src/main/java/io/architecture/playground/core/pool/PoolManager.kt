package io.architecture.playground.core.pool

import android.util.Log
import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.local.model.TraceEntity
import io.architecture.playground.model.Node
import io.architecture.playground.model.Trace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

typealias PoolMap = Map<KClass<out PoolMember>, PoolObjects<out PoolMember>>

@Suppress("UNCHECKED_CAST")
@Singleton
class PoolManager @Inject constructor(
    poolNodesFactory: PoolObjects.Factory<Node>,
    poolTracesFactory: PoolObjects.Factory<Trace>,
    poolNodeEntitiesFactory: PoolObjects.Factory<NodeEntity>,
    poolTraceEntitiesFactory: PoolObjects.Factory<TraceEntity>,
    private val initializer: PoolMemoryInitializer,
    monitor: PoolMonitor
) {
    val holder: PoolMap = mapOf(
        // TODO Implement delegate or something else. Do not have enumeration here
        Node::class to poolNodesFactory.create(100_000),
        Trace::class to poolTracesFactory.create(100_000),
        NodeEntity::class to poolNodeEntitiesFactory.create(100_000),
        TraceEntity::class to poolTraceEntitiesFactory.create(100_000),
    )

    var isInitialized = false

    init {
        monitor.start(holder)
    }

    suspend fun initialize() {
        withContext(Dispatchers.Default) {
            initializer.initializeWith(
                holder[Node::class] as PoolObjects<Node>,
                holder[Trace::class] as PoolObjects<Trace>,
                holder[NodeEntity::class] as PoolObjects<NodeEntity>,
                holder[TraceEntity::class] as PoolObjects<TraceEntity>
            )
            Log.d("POOL_OBJECTS", "Pools initialized!!")
            isInitialized = true
        }
    }

    inline fun <reified T : PoolMember> getPoolByMember() = holder[T::class] as PoolObjects<T>

    inline fun <reified T : PoolMember> getPoolBy(classKey: KClass<T>): PoolObjects<T> =
        holder[classKey] as PoolObjects<T>

}