package io.architecture.data.repository

import io.architecture.common.DefaultDispatcher
import io.architecture.common.IoDispatcher
import io.architecture.data.mapping.toExternal
import io.architecture.data.repository.interfaces.RouteRepository
import io.architecture.database.api.model.toExternal
import io.architecture.database.api.model.toLocal
import io.architecture.datasource.api.LocalDataSource
import io.architecture.datasource.api.NetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultRouteRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localTraceRouteDataSource: LocalDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : RouteRepository {

    override suspend fun add(route: io.architecture.model.Route) = withContext(ioDispatcher) {
        localTraceRouteDataSource.createOrUpdate(route.toLocal())
    }

    override fun streamAndPersist() =
        networkDataSource.streamRoutes()
            .map { it.toExternal() }
            .flowOn(defaultDispatcher)
            .onEach { localTraceRouteDataSource.createOrUpdate(it.toLocal()) }
            .flowOn(ioDispatcher)

    override suspend fun getRouteBy(nodeId: String) = withContext(ioDispatcher) {
        localTraceRouteDataSource.getRouteBy(nodeId)?.toExternal()
    }
}