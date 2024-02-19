package io.architecture.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.architecture.api.dao.NodeDao
import io.architecture.api.dao.RouteDao
import io.architecture.api.dao.TraceDao
import io.architecture.api.model.NodeEntity
import io.architecture.api.model.RouteEntity
import io.architecture.api.model.TraceEntity
import io.architecture.database.imp.room.convertors.DateTypeConvertor
import io.architecture.database.imp.room.convertors.ListCoordinatesTypeConvertor

@Database(
    entities = [NodeEntity::class, TraceEntity::class, RouteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    ListCoordinatesTypeConvertor::class,
    DateTypeConvertor::class
)
abstract class InMemoryDatabase : RoomDatabase() {

    abstract fun nodeDao(): NodeDao

    abstract fun traceDao(): TraceDao

    abstract fun routeDao(): RouteDao
}