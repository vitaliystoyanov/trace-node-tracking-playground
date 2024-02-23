package io.architecture.database.imp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.architecture.database.api.dao.InterfaceRouteDao
import io.architecture.database.imp.room.entity.RoomRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class RouteDao : InterfaceRouteDao<RoomRouteEntity> {

    @Query("SELECT * FROM routes WHERE node_id = :nodeId")
    abstract override suspend fun getById(nodeId: String): RoomRouteEntity

    @Query("SELECT COUNT(node_id) FROM routes")
    abstract override fun observeCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insert(node: RoomRouteEntity)

    @Query("DELETE FROM routes")
    abstract override suspend fun deleteAll()

}