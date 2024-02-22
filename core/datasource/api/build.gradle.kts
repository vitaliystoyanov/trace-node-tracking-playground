@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.database.api)
    implementation(projects.core.network.websocket.api)

    implementation(libs.kotlinx.coroutine.core)
}