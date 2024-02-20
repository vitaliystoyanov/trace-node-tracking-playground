package io.architecture.database.imp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.architecture.database.api.dao.InterfaceTraceDao
import io.architecture.database.imp.room.entity.RoomTraceEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class TraceDao : InterfaceTraceDao<RoomTraceEntity> {

    @Query("SELECT * FROM traces")
    abstract override fun observeAll(): Flow<List<RoomTraceEntity>>

    @Query(
        """
        SELECT node_id, lon, lat, sentAtTime, speed, azimuth, alt, MAX(sentAtTime) AS sentAtTime 
            FROM traces
        WHERE node_id = :nodeId LIMIT 1
    """
    )
    abstract override fun observeById(nodeId: String): Flow<RoomTraceEntity>

    @Query("SELECT COUNT(node_id) FROM traces")
    abstract override fun observeCount(): Flow<Int>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insert(trace: RoomTraceEntity)

    @Query(
        """
        INSERT INTO traces (node_id, lon, lat, speed, azimuth, alt, sentAtTime) 
            VALUES(:node_id, :lon, :lat, :speed, :azimuth, :alt, :time)
    """
    )
    abstract override suspend fun insert(
        node_id: String,
        lon: Double,
        lat: Double,
        speed: Int,
        azimuth: Double,
        alt: Double,
        time: Long,
    )

    @Query("DELETE FROM traces WHERE node_id = :nodeId")
    abstract override suspend fun deleteById(nodeId: String): Int

    @Query("DELETE FROM traces")
    abstract override suspend fun deleteAll()

}