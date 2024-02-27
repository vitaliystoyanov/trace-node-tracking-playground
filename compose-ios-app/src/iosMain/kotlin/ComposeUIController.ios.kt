import androidx.compose.ui.window.ComposeUIViewController
import io.architecture.core.di.coreKoinModules
import io.architecture.datasource.api.emptyMockLocalDatasourceModule
import io.architecture.feature.common.map.MapScreen
import org.koin.compose.KoinApplication

fun controller() = ComposeUIViewController {
    KoinApplication(application = {
        modules(coreKoinModules, emptyMockLocalDatasourceModule)
    }) {
        MapScreen()
    }
}