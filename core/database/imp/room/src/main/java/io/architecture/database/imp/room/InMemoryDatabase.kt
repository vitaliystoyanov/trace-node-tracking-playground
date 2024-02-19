package io.architecture.database.imp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.architecture.database.api.dao.NodeDao
import io.architecture.database.api.dao.RouteDao
import io.architecture.database.api.dao.TraceDao
import io.architecture.database.api.model.NodeEntity
import io.architecture.database.api.model.RouteEntity
import io.architecture.database.api.model.TraceEntity
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
internal abstract class InMemoryDatabase : RoomDatabase() {

    abstract fun nodeDao(): NodeDao

    abstract fun traceDao(): TraceDao

    abstract fun routeDao(): RouteDao
}