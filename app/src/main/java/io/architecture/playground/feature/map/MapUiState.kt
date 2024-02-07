package io.architecture.playground.feature.map

import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.model.Route

class MapUiState(
    var displayRoute: Route?,
    var connectionState: ConnectionState,
)