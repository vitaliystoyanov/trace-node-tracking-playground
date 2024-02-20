package io.architecture.database.api.dao

import io.architecture.database.api.model.NodeEntity
import kotlinx.coroutines.flow.Flow

interface InterfaceNodeDao<T : NodeEntity> {

    fun observeAll(): Flow<List<T>>

    suspend fun insert(node: T)

    suspend fun update(node: T)

    fun observeCount(): Flow<Int>

    suspend fun delete(node: T)

}