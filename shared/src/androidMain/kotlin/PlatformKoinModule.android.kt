import android.os.Build
import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual val platformModule: Module = module {
    single<Platform> { AndroidPlatform() }
}