package io.architecture.playground.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.local.TraceDao
import io.architecture.playground.data.local.TraceDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): TraceDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            TraceDatabase::class.java
        ).build()
    }

    @Provides
    fun provideTraceDao(database: TraceDatabase): TraceDao = database.traceDao()

}