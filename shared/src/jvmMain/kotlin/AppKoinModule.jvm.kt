import io.architecture.core.di.coreKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

actual val appModule: Module = module {
    includes(coreKoinModules)
}