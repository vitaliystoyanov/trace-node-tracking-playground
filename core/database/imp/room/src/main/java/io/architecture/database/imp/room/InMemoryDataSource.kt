package io.architecture.database.imp.room

import io.architecture.database.api.model.NodeEntity
import io.architecture.database.api.model.RouteEntity
import io.architecture.database.api.model.TraceEntity
import io.architecture.database.imp.room.dao.NodeDao
import io.architecture.database.imp.room.dao.RouteDao
import io.architecture.database.imp.room.dao.TraceDao
import io.architecture.database.imp.room.entity.RoomNodeEntity
import io.architecture.database.imp.room.entity.RoomRouteEntity
import io.architecture.database.imp.room.entity.RoomTraceEntity
import io.architecture.datasource.api.LocalDataSource
import kotlinx.coroutines.flow.Flow

internal class InMemoryDataSource(
    private val nodeDao: NodeDao,
    private val traceDao: TraceDao,
    private val routeDao: RouteDao,
) : LocalDataSource {

    override fun observeAllNodes(): Flow<List<NodeEntity>> = nodeDao.observeAll()

    override fun observeAllTraces(): Flow<List<TraceEntity>> = traceDao.observeAll()

    override fun observeNodeCount(): Flow<Int> = nodeDao.observeCount()

    override suspend fun getRouteBy(nodeId: String): RouteEntity? = routeDao.getById(nodeId)

    override suspend fun createOrUpdate(node: NodeEntity) =
        nodeDao.insert(RoomNodeEntity.create(node))

    override suspend fun createOrUpdate(trace: TraceEntity) =
        traceDao.insert(RoomTraceEntity.create(trace))

    override suspend fun createOrUpdate(route: RouteEntity) =
        routeDao.insert(RoomRouteEntity.create(route))

    override suspend fun deleteAllTraces() = traceDao.deleteAll()

    override fun observeTraceCount(): Flow<Int> = traceDao.observeCount()

    override fun observeTraceBy(nodeId: String): Flow<TraceEntity> =
        traceDao.observeById(nodeId)
}
