package io.architecture.core.runtime.configuration

actual val platform: IPlatform = object : IPlatform {
    override val name: String = "JS browser"
}