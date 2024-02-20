package io.architecture.database.imp.room

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.architecture.database.imp.room.dao.NodeDao
import io.architecture.database.imp.room.dao.RouteDao
import io.architecture.database.imp.room.dao.TraceDao
import io.architecture.datasource.api.LocalDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataBaseModule {

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

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DatasourceModule {

    @Singleton
    @Binds
    abstract fun bindLocalDataSource(dataSource: InMemoryDataSource): LocalDataSource
}