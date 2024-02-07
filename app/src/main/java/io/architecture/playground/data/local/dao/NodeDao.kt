package io.architecture.playground.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.local.model.NodeWithRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NodeDao {

    @Query("SELECT * FROM nodes")
    fun observeAll(): Flow<List<NodeEntity>>

    @Query("SELECT * FROM nodes WHERE node_id = :id")
    fun observeById(id: String): Flow<NodeEntity>

    @Query("SELECT COUNT(node_id) FROM nodes")
    fun observeCountNodes(): Flow<Int>

    @Query("SELECT * FROM nodes")
    suspend fun getAll(): List<NodeEntity>

    @Query("SELECT * FROM nodes WHERE node_id = :id")
    suspend fun getAllBy(id: String): List<NodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(node: NodeEntity)

    @Query("DELETE FROM nodes WHERE node_id = :id")
    suspend fun deleteById(id: String): Int

    @Query("DELETE FROM nodes")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM nodes")
    fun observeAllNodeWithRoute(): Flow<List<NodeWithRouteEntity>>

}