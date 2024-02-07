package io.architecture.playground.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.architecture.playground.data.local.convertors.DateTypeConvertor
import io.architecture.playground.data.local.convertors.ListCoordinatesTypeConvertor
import io.architecture.playground.data.local.dao.NodeDao
import io.architecture.playground.data.local.dao.RouteDao
import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.local.model.RouteEntity

@Database(entities = [NodeEntity::class, RouteEntity::class], version = 1, exportSchema = false)
@TypeConverters(ListCoordinatesTypeConvertor::class, DateTypeConvertor::class)
abstract class NodeDatabase : RoomDatabase() {
    abstract fun nodeDao(): NodeDao

    abstract fun routeDao(): RouteDao
}