package io.architecture.playground.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.architecture.playground.data.DiverTraceRepository
import io.architecture.playground.data.remote.model.NetworkConnectionEvent
import io.architecture.playground.data.remote.model.NetworkConnectionEventType
import io.architecture.playground.model.DiverTrace
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DiverUiState(
    var trace: DiverTrace,
    var historyTraces: List<DiverTrace>,
    var connection: NetworkConnectionEvent
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val diverTraceRepository: DiverTraceRepository
) : ViewModel() {

    private var trace = flowOf(DiverTrace())
    private var historyTraces = diverTraceRepository.getStreamDiverTraceHistory()
    private var connection = diverTraceRepository.observeConnection()

    val uiState: StateFlow<DiverUiState> =
        combine(trace, historyTraces, connection) { trace, history, connection ->
            DiverUiState(trace, history, connection)
        }.filter { it.historyTraces.isNotEmpty() }
            .map { it.trace = it.historyTraces.last(); it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DiverUiState(
                    DiverTrace(),
                    emptyList(),
                    NetworkConnectionEvent(NetworkConnectionEventType.ConnectionUndefined)
                )
            )
}