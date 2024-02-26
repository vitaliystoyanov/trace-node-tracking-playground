import androidx.compose.ui.window.ComposeUIViewController
import org.koin.compose.KoinApplication

fun controller() = ComposeUIViewController {
    ComposeApplication()
}