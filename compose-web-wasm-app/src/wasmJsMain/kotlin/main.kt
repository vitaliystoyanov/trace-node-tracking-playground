import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.architecture.core.di.coreKoinModules
import io.architecture.datasource.api.di.emptyMockLocalDatasourceModule
import io.architecture.datasource.api.di.inMemoryLocalDatasourceModule
import io.architecture.feature.common.map.MapScreen
import io.architecture.feature.common.map.di.mapFeatureModule
import org.koin.compose.KoinApplication

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        KoinApplication(application = {
            modules(coreKoinModules, mapFeatureModule, inMemoryLocalDatasourceModule)
        }) {
            MapScreen()
        }
    }
}