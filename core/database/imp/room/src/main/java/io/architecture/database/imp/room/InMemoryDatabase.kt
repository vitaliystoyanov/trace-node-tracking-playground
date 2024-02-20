package io.architecture.database.imp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.architecture.database.imp.room.convertors.DateTypeConvertor
import io.architecture.database.imp.room.convertors.ListCoordinatesTypeConvertor
import io.architecture.database.imp.room.dao.NodeDao
import io.architecture.database.imp.room.dao.RouteDao
import io.architecture.database.imp.room.dao.TraceDao
import io.architecture.database.imp.room.entity.RoomNodeEntity
import io.architecture.database.imp.room.entity.RoomRouteEntity
import io.architecture.database.imp.room.entity.RoomTraceEntity

@Database(
    entities = [RoomNodeEntity::class, RoomTraceEntity::class, RoomRouteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    ListCoordinatesTypeConvertor::class,
    DateTypeConvertor::class
)
internal abstract class InMemoryDatabase : RoomDatabase() {

    abstract fun nodeDao(): NodeDao

    abstract fun traceDao(): TraceDao

    abstract fun routeDao(): RouteDao
}