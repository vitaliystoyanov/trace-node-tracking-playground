import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.architecture.core.runtime.configuration.Runtime
import io.architecture.model.Connection
import io.architecture.model.UpstreamRtt
import io.architecture.ui.StatusBar
import org.koin.compose.koinInject

@Composable
fun AppContainer() {
    MaterialTheme {
        StatusBar(
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth(),
            Connection(Connection.State.FAILED, rtt = UpstreamRtt(0)),
            0
        )
    }
}