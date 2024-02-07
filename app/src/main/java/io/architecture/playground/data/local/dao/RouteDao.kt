package io.architecture.playground.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.architecture.playground.data.local.model.RouteEntity

@Dao
interface RouteDao {

    @Query("SELECT * FROM node_routes WHERE node_id = :id")
    suspend fun getById(id: String): RouteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(node: RouteEntity)

    @Query("DELETE FROM node_routes")
    suspend fun deleteAll()

}