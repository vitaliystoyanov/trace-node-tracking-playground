@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}


dependencies {
    implementation(projects.core.common)
    implementation(projects.core.database.api)
    implementation(projects.core.datasource.api)
    implementation(projects.core.model)
    implementation(projects.core.network.websocket.api)
    implementation(projects.core.runtime.logging)

    implementation(libs.kotlinx.coroutine.core)

    // Koin
    implementation(libs.koin.core)
    ksp(libs.koin.ksp.compiler)

    testImplementation(libs.junit.junit)
}