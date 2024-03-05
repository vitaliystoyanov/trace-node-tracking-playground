package io.architecture.core.runtime.configuration

interface IPlatform {
    val name: String
}

expect val platform: IPlatform