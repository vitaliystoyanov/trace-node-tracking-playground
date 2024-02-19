package io.architecture.database.api.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.architecture.database.api.model.TraceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TraceDao {

    @Query("SELECT * FROM traces")
    fun observeAll(): Flow<List<TraceEntity>>

    @Query(
        """
        SELECT node_id, lon, lat, sentAtTime, speed, azimuth, alt, MAX(sentAtTime) AS sentAtTime 
            FROM traces
        WHERE node_id = :nodeId LIMIT 1
    """
    )
    fun observeById(nodeId: String): Flow<TraceEntity>

    @Query("SELECT COUNT(node_id) FROM traces")
    fun observeCount(): Flow<Int>

    @Query("SELECT * FROM traces")
    suspend fun getAll(): List<TraceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trace: TraceEntity)

    @Query(
        """
        INSERT INTO traces (node_id, lon, lat, speed, azimuth, alt, sentAtTime) 
            VALUES(:node_id, :lon, :lat, :speed, :azimuth, :alt, :time)
    """
    )
    suspend fun insert(
        node_id: String,
        lon: Double,
        lat: Double,
        speed: Int,
        azimuth: Double,
        alt: Double,
        time: Long,
    )

    @Query("DELETE FROM traces WHERE node_id = :nodeId")
    suspend fun deleteById(nodeId: String): Int

    @Query("DELETE FROM traces")
    suspend fun deleteAll()

}