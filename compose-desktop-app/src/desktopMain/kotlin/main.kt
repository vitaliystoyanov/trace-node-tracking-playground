import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.architecture.core.design.system.theme.UAVTheme
import io.architecture.core.di.coreKoinModules
import io.architecture.datasource.api.di.inMemoryLocalDatasourceModule
import io.architecture.feature.common.map.MapScreen
import io.architecture.feature.common.map.di.mapFeatureModule
import org.koin.compose.KoinApplication

fun main() = application {
    val state = rememberWindowState(size = DpSize(1680.dp, 840.dp))
    Window(onCloseRequest = ::exitApplication, state, title = "Node trace tracking") {
        KoinApplication(application = {
            modules(coreKoinModules, mapFeatureModule, inMemoryLocalDatasourceModule)
        }) {
            UAVTheme {
                MapScreen()
            }
        }
    }
}