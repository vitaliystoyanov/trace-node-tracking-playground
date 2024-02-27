import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.domain.GetConnectionStateUseCase
import io.architecture.domain.GetStreamChunkedNodeWithTraceUseCase
import io.architecture.model.Connection
import io.architecture.model.Trace
import io.architecture.ui.StatusBar
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import kotlin.time.Duration.Companion.seconds

@Composable
fun ComposeApplication() {
    KoinApplication(application = {
        modules(appModule + mockLocalDatasourceModule)
    }) {
        // https://developer.android.com/jetpack/compose/side-effects?hl=en#remembercoroutinescope
        // rememberCoroutineScope is a composable function that returns a CoroutineScope bound
        // to the point of the Composition where it's called.
        // The scope will be cancelled when the call leaves the Composition.
        val scope = rememberCoroutineScope()

        val getChunkedNodeWithTrace = koinInject<GetStreamChunkedNodeWithTraceUseCase>()
        val nodeRepository = koinInject<NodeRepository>()
        val connectionState = koinInject<GetConnectionStateUseCase>()
        val connectionsUiState = connectionState()
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = io.architecture.model.Connection(
                    rtt = io.architecture.model.UpstreamRtt(
                        0L
                    ), isConnected = false
                )
            )

        val nodeCounterUiState: StateFlow<Int> = nodeRepository.streamCount()
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

        val tracesUiState: StateFlow<Sequence<Trace>> =
            getChunkedNodeWithTrace(isDatabaseOutgoingStream = false, interval = 1.seconds)
                .onEach {
                    println( // TODO Logger module
                        "REPOSITORY_DEBUG_N: " +
                                "chunked list size of traces to sequence - ${it.count()}"
                    )
                }
                .stateIn(
                    scope = scope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptySequence()
                )

        /* TODO produceState: convert non-Compose state into Compose state:

           https://developer.android.com/jetpack/compose/side-effects?hl=en#producestate

           Some example:
           https://github.com/joreilly/PeopleInSpace/blob/724b1150b774d552adbfeb5ef97f3117036d1864/compose-web/src/jsMain/kotlin/Main.kt#L30

         */
        StatusBarContainer(tracesUiState, nodeCounterUiState, connectionsUiState)
    }
}

@Composable
fun StatusBarContainer(
    tracesUiState: StateFlow<Sequence<Trace>>,
    nodeCounterUiState: StateFlow<Int>,
    connectionsUiState: StateFlow<Connection>,
) {
    val connection = connectionsUiState.collectAsState().value
    val nodeCount = nodeCounterUiState.collectAsState().value
    val traces =
        tracesUiState.collectAsState().value.toList().size // TODO Temporary to track count
    StatusBar(
        modifier = Modifier
            .wrapContentSize()
            .fillMaxWidth()
            .height(20.dp),
        connection,
        traces
    )
}

