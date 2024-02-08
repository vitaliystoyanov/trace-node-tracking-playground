package io.architecture.playground.feature.map

import io.architecture.playground.model.Node
import io.architecture.playground.model.Trace

class MapNodesUiState(
    var nodes: Set<Pair<Node, Trace>>,
    var nodeCount: Int,
)