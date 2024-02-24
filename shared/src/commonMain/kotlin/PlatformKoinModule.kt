import org.koin.core.module.Module

interface Platform {
    val name: String
}

expect val platformModule: Module