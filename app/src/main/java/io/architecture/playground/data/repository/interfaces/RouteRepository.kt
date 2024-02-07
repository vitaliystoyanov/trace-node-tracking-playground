package io.architecture.playground.data.repository.interfaces

import io.architecture.playground.model.Route
import kotlinx.coroutines.flow.Flow

interface RouteRepository {

    suspend fun add(route: Route)

    fun observeAndStoreRoutes(): Flow<Route>

    suspend fun getRouteBy(nodeId: String): Route?
}