package io.architecture.database.imp.room.di

import io.architecture.database.imp.room.InMemoryDataSource
import io.architecture.database.imp.room.InMemoryDatabase
import io.architecture.datasource.api.LocalDataSource
import org.koin.dsl.module

val roomDaoModule = module {
    includes(roomDatabaseModule)

    single { get<InMemoryDatabase>().nodeDao() }
    single { get<InMemoryDatabase>().traceDao() }
    single { get<InMemoryDatabase>().routeDao() }

    single<LocalDataSource> { InMemoryDataSource(get(), get(), get()) }
}