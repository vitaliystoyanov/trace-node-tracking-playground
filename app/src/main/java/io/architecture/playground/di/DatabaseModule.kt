package io.architecture.playground.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.local.NodeDatabase
import io.architecture.playground.data.local.dao.NodeDao
import io.architecture.playground.data.local.dao.RouteDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): NodeDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            NodeDatabase::class.java
        ).build()
    }

    @Provides
    fun provideNodeDao(database: NodeDatabase): NodeDao = database.nodeDao()

    @Provides
    fun provideRouteDao(database: NodeDatabase): RouteDao = database.routeDao()
}