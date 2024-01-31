package io.architecture.playground.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalDiverTrace::class], version = 1, exportSchema = false)
abstract class DiverDatabase : RoomDatabase() {

    abstract fun diverTraceDao(): DiverTraceDao
}