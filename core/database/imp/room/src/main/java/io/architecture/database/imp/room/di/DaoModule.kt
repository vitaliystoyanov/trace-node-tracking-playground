package io.architecture.database.imp.room.di

import io.architecture.database.imp.room.InMemoryDataSource
import io.architecture.database.imp.room.InMemoryDatabase
import io.architecture.datasource.api.LocalDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val roomDaoModule = module {
    includes(roomDatabaseModule)

    single { get<InMemoryDatabase>().nodeDao() }
    single { get<InMemoryDatabase>().traceDao() }
    single { get<InMemoryDatabase>().routeDao() }

    singleOf(::InMemoryDataSource ) bind LocalDataSource::class
}