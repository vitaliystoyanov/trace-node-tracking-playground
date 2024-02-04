package io.architecture.playground.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalNode::class], version = 1, exportSchema = false)
abstract class NodeDatabase : RoomDatabase() {
    abstract fun nodeDao(): NodeDao

}