package io.architecture.playground.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.architecture.playground.data.local.model.NodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NodeDao {

    @Query("SELECT * FROM nodes")
    fun observeAll(): Flow<List<NodeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(node: NodeEntity)

    @Update
    suspend fun update(node: NodeEntity)

    @Query("SELECT COUNT(id) FROM nodes")
    fun observeCount(): Flow<Int>

    @Delete
    suspend fun delete(node: NodeEntity)

}