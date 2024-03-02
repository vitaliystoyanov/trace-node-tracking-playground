import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.architecture.core.di.coreKoinModules
import io.architecture.datasource.api.di.inMemoryLocalDatasourceModule
import io.architecture.feature.common.map.MapScreen
import io.architecture.feature.common.map.di.mapFeatureModule
import org.koin.compose.KoinApplication

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Node trace tracking") {
        KoinApplication(application = {
            modules(coreKoinModules, mapFeatureModule, inMemoryLocalDatasourceModule)
        }) {
            MapScreen()
        }
    }
}