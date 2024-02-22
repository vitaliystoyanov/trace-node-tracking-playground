package io.architecture.data.repository.interfaces

import io.architecture.model.Route
import kotlinx.coroutines.flow.Flow

interface RouteRepository {

    suspend fun add(route: Route)

    suspend fun getRouteBy(nodeId: String): Route?

    fun streamRoutes(isPersisted: Boolean): Flow<Route>
}