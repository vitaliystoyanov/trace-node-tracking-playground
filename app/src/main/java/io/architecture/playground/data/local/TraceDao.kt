package io.architecture.playground.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TraceDao {

    @Query("SELECT * FROM traces")
    fun observeAll(): Flow<List<LocalTrace>>

    @Query("SELECT * FROM traces WHERE id = :nodeId")
    fun observeById(nodeId: String): Flow<LocalTrace>

    @Query("SELECT * FROM traces ORDER BY datetime(time) DESC LIMIT 1")
    fun observeLatest(): Flow<LocalTrace>

    @Query("SELECT MAX(id) as id,lon,lat,speed,bearing,alt,time,nodeId,mode FROM traces GROUP BY nodeId")
    fun observeLatestTraceByUniqNodeIds(): Flow<List<LocalTrace>>

    @Query("SELECT COUNT(id) FROM traces")
    fun observeCountTraces(): Flow<Long>

    @Query("SELECT * FROM traces")
    suspend fun getAll(): List<LocalTrace>

    @Query("SELECT * FROM traces WHERE nodeId = :nodeId")
    suspend fun getAllById(nodeId: String): List<LocalTrace>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trace: LocalTrace)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(traces: List<LocalTrace>)

    @Query("DELETE FROM traces WHERE id = :nodeId")
    suspend fun deleteById(nodeId: String): Int

    @Query("DELETE FROM traces")
    fun deleteAll()

}