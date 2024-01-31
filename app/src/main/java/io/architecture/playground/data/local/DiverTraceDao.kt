package io.architecture.playground.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DiverTraceDao {

    @Query("SELECT * FROM diver_traces")
    fun observeAll(): Flow<List<LocalDiverTrace>>

    @Query("SELECT * FROM diver_traces WHERE id = :diverId")
    fun observeById(diverId: String): Flow<LocalDiverTrace>

    @Query("SELECT * FROM diver_traces")
    suspend fun getAll(): List<LocalDiverTrace>

    @Query("SELECT * FROM diver_traces WHERE id = :diverId")
    suspend fun getById(diverId: String): LocalDiverTrace?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trace: LocalDiverTrace)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(traces: List<LocalDiverTrace>)

    @Query("DELETE FROM diver_traces WHERE id = :diverId")
    suspend fun deleteById(diverId: String): Int

    @Query("DELETE FROM diver_traces")
    suspend fun deleteAll()

}