import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.architecture.core.di.coreKoinModules
import io.architecture.datasource.api.emptyMockLocalDatasourceModule
import io.architecture.feature.common.map.MapScreen
import org.koin.compose.KoinApplication

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        KoinApplication(application = {
            modules(coreKoinModules, emptyMockLocalDatasourceModule)
        }) {
            MapScreen()
        }
    }
}