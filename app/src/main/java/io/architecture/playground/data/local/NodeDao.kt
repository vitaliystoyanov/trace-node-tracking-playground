package io.architecture.playground.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NodeDao {

    @Query("SELECT * FROM nodes")
    fun observeAll(): Flow<List<LocalNode>>

    @Query("SELECT * FROM nodes WHERE nodeId = :id")
    fun observeById(id: String): Flow<LocalNode>

    @Query("SELECT * FROM nodes ORDER BY datetime(time) DESC LIMIT 1")
    fun observeLatest(): Flow<LocalNode>

    @Query("SELECT COUNT(nodeId) FROM nodes")
    fun observeCountNodes(): Flow<Long>

    @Query("SELECT * FROM nodes")
    suspend fun getAll(): List<LocalNode>

    @Query("SELECT * FROM nodes WHERE nodeId = :id")
    suspend fun getAllBy(id: String): List<LocalNode>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(node: LocalNode)

    @Query("DELETE FROM nodes WHERE nodeId = :id")
    suspend fun deleteById(id: String): Int

    @Query("DELETE FROM nodes")
    fun deleteAll()

}