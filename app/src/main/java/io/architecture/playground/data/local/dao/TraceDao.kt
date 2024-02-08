package io.architecture.playground.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.architecture.playground.data.local.model.TraceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TraceDao {

    @Query("SELECT * FROM traces")
    fun observeAll(): Flow<List<TraceEntity>>

    @Query("SELECT * FROM traces WHERE node_id = :nodeId")
    fun observeById(nodeId: String): Flow<TraceEntity>

    @Query("SELECT * FROM traces")
    suspend fun getAll(): List<TraceEntity>

    @Query("SELECT * FROM traces WHERE node_id = :nodeId")
    suspend fun getAllBy(nodeId: String): List<TraceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trace: TraceEntity)

    @Query("DELETE FROM traces WHERE node_id = :nodeId")
    suspend fun deleteById(nodeId: String): Int

    @Query("DELETE FROM traces")
    suspend fun deleteAll()

}