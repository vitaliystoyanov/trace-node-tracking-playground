import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.compose.KoinApplication

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "KotlinProject") {
        KoinApplication(application = {
            modules(appModule)
        }) {
            AppContainer()
        }
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    AppContainer()
}