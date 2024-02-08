package io.architecture.playground.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.local.InMemoryDatabase
import io.architecture.playground.data.local.dao.NodeDao
import io.architecture.playground.data.local.dao.TraceDao
import io.architecture.playground.data.local.dao.RouteDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideInMemoryDatabase(@ApplicationContext context: Context): InMemoryDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            InMemoryDatabase::class.java
        ).build()
    }

    @Provides
    @Singleton
    fun provideNodeDao(database: InMemoryDatabase): NodeDao = database.nodeDao()

    @Provides
    @Singleton
    fun provideTraceDao(database: InMemoryDatabase): TraceDao = database.traceDao()

    @Provides
    @Singleton
    fun provideRouteDao(database: InMemoryDatabase): RouteDao = database.routeDao()
}