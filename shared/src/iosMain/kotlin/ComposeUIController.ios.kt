import androidx.compose.ui.window.ComposeUIViewController
import org.koin.compose.KoinApplication

fun controller() = ComposeUIViewController {
    KoinApplication(application = {
        modules(appModule)
    }) {
        AppContainer()
    }
}