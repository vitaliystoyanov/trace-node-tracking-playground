package io.architecture.database.api.dao

import io.architecture.database.api.model.TraceEntity
import kotlinx.coroutines.flow.Flow

interface InterfaceTraceDao<T : TraceEntity> {

    fun observeAll(): Flow<List<T>>

    fun observeById(nodeId: String): Flow<T>

    fun observeCount(): Flow<Int>

    suspend fun insert(trace: T)

    suspend fun insert(
        node_id: String,
        lon: Double,
        lat: Double,
        speed: Int,
        azimuth: Double,
        alt: Double,
        time: Long,
    )

    suspend fun deleteById(nodeId: String): Int

    suspend fun deleteAll()

}