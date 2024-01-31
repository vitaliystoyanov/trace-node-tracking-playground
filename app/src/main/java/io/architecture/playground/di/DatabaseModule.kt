package io.architecture.playground.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.local.DiverTraceDao
import io.architecture.playground.data.local.DiverDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): DiverDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DiverDatabase::class.java,
            "DiverTraces.db"
        ).build()
    }

    @Provides
    fun provideDiverDao(database: DiverDatabase): DiverTraceDao = database.diverTraceDao()
}