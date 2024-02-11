package io.architecture.playground.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.local.model.NodeWithLastTraceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NodeDao {

    @Query("SELECT * FROM nodes")
    fun observeAll(): Flow<List<NodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(node: NodeEntity)

    @Update
    suspend fun update(node: NodeEntity)

    @Query("SELECT COUNT(id) FROM nodes")
    fun observeCount(): Flow<Int>

    @Delete
    suspend fun delete(node: NodeEntity)

    @Query("SELECT * FROM nodes, traces WHERE nodes.id = traces.node_id")
    fun observeAllWithLastTrace(): Flow<List<NodeWithLastTraceEntity>>

    @Query("SELECT * FROM nodes, traces WHERE nodes.id = traces.node_id")
    suspend fun getAllWithLastTrace(): List<NodeWithLastTraceEntity>
}