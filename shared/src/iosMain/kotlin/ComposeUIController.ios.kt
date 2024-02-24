import androidx.compose.ui.window.ComposeUIViewController
import org.koin.compose.KoinApplication
import org.koin.core.KoinApplication

fun controller() = ComposeUIViewController {
    KoinApplication(application = {
        modules(appModule)
    }) {
        AppContainer()
    }
}