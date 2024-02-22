package io.architecture.data.repository

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
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

class DefaultRouteRepository(
    private val networkDataSource: NetworkDataSource,
    private val localTraceRouteDataSource: LocalDataSource,
    @Named("defaultDispatcher") private val defaultDispatcher: CoroutineDispatcher,
    @Named("ioDispatcher") private val ioDispatcher: CoroutineDispatcher,
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