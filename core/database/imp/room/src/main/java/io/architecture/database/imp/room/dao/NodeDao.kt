package io.architecture.database.imp.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.architecture.database.api.dao.InterfaceNodeDao
import io.architecture.database.imp.room.entity.RoomNodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class NodeDao : InterfaceNodeDao<RoomNodeEntity> {

    @Query("SELECT * FROM nodes")
    abstract override fun observeAll(): Flow<List<RoomNodeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract override suspend fun insert(node: RoomNodeEntity)

    @Update
    abstract override suspend fun update(node: RoomNodeEntity)

    @Query("SELECT COUNT(id) FROM nodes")
    abstract override fun observeCount(): Flow<Int>

    @Delete
    abstract override suspend fun delete(node: RoomNodeEntity)

}