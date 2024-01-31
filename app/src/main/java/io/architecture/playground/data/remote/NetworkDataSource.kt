package io.architecture.playground.data.remote

import kotlinx.coroutines.flow.Flow


interface NetworkDataSource {

    fun streamDiverTraces(): Flow<NetworkDiverTrace>

}