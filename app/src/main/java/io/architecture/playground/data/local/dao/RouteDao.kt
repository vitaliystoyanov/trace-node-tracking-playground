package io.architecture.playground.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.architecture.playground.data.local.model.RouteEntity

@Dao
interface RouteDao {

    @Query("SELECT * FROM routes WHERE node_id = :nodeId")
    suspend fun getById(nodeId: String): RouteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(node: RouteEntity)

    @Query("DELETE FROM routes")
    suspend fun deleteAll()

}