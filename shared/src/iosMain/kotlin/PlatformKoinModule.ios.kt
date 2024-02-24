import org.koin.core.module.Module
import org.koin.dsl.module
import platform.UIKit.UIDevice

class IosPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual val platformModule: Module = module {
    single<Platform> { IosPlatform() }
}