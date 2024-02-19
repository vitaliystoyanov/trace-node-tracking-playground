package io.architecture.database.imp.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.architecture.database.api.dao.NodeDao
import io.architecture.database.api.dao.RouteDao
import io.architecture.database.api.dao.TraceDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

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