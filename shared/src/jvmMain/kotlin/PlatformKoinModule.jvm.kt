import org.koin.core.module.Module
import org.koin.dsl.module

class JvmPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual val platformModule: Module = module {
    single<Platform> { JvmPlatform() }
}