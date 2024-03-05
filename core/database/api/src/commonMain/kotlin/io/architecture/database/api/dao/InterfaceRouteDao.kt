package io.architecture.database.api.dao

import io.architecture.database.api.model.RouteEntity
import kotlinx.coroutines.flow.Flow

interface InterfaceRouteDao<T : RouteEntity> {

    suspend fun getById(nodeId: String): T

    fun observeCount(): Flow<Int>

    suspend fun insert(node: T)

    suspend fun deleteAll()

}