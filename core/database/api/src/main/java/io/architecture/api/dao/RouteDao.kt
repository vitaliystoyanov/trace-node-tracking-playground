package io.architecture.api.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.architecture.api.model.RouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {

    @Query("SELECT * FROM routes WHERE node_id = :nodeId")
    suspend fun getById(nodeId: String): RouteEntity

    @Query("SELECT COUNT(node_id) FROM routes")
    fun observeCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(node: RouteEntity)

    @Query("DELETE FROM routes")
    suspend fun deleteAll()

}