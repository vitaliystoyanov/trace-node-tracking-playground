@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.runtime.logging)

    implementation(libs.kotlinx.coroutine.core)

    // Koin
    implementation(libs.koin.core)
    ksp(libs.koin.ksp.compiler)
}