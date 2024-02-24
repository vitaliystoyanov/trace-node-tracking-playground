import io.architecture.core.runtime.configuration.runtimeModule
import org.koin.core.module.Module
import org.koin.dsl.module

actual val appModule: Module = module {
    includes(
//        coreKoinModules, will be soon here
        runtimeModule,
    )
}