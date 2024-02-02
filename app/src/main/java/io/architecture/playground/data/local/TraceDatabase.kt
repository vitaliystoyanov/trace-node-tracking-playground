package io.architecture.playground.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalTrace::class], version = 1, exportSchema = false)
abstract class TraceDatabase : RoomDatabase() {
    abstract fun traceDao(): TraceDao
}