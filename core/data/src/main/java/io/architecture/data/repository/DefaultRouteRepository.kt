package io.architecture.data.repository

import io.architecture.data.mapping.toExternal
import io.architecture.data.repository.interfaces.RouteRepository
import io.architecture.database.api.model.toExternal
import io.architecture.database.api.model.toLocal
import io.architecture.datasource.api.LocalDataSource
import io.architecture.datasource.api.NetworkDataSource
import io.architecture.model.Route
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class DefaultRouteRepository(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource,
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) : RouteRepository {

    override suspend fun add(route: Route) = withContext(ioDispatcher) {
        localDataSource.createOrUpdate(route.toLocal())
    }

    override fun streamRoutes(isPersisted: Boolean) =
        networkDataSource.streamRoutes()
            .map { it.toExternal() }
            .flowOn(defaultDispatcher)
            .onEach { if (isPersisted) localDataSource.createOrUpdate(it.toLocal()) }
            .flowOn(ioDispatcher)

    override suspend fun getRouteBy(nodeId: String) = withContext(ioDispatcher) {
        localDataSource.getRouteBy(nodeId)?.toExternal()
    }
}