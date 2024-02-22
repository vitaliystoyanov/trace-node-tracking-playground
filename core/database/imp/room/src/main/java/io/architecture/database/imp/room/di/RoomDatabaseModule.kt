package io.architecture.database.imp.room.di

import androidx.room.Room
import io.architecture.database.imp.room.InMemoryDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val roomDatabaseModule = module {
    single {
        Room.inMemoryDatabaseBuilder(
            androidApplication(),
            InMemoryDatabase::class.java
        ).build()
    }
}