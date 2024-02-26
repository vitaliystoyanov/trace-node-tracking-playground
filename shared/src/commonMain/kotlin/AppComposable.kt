import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.architecture.model.Connection
import io.architecture.model.UpstreamRtt
import io.architecture.ui.StatusBar

@Composable
fun AppContainer() {
    StatusBar(
        modifier = Modifier
            .wrapContentSize()
            .fillMaxWidth()
            .height(20.dp),
        Connection(Connection.State.FAILED, rtt = UpstreamRtt(0)),
        0
    )
}