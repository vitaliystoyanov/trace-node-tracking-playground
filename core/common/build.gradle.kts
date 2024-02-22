@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(projects.core.runtime.logging)

    implementation(libs.kotlinx.coroutine.android)
    testImplementation(libs.junit.junit)
}