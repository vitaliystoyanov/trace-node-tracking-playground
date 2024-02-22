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
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.network.websocket.imp.ktor)
    implementation(projects.core.runtime.configuration)

    implementation(libs.kotlinx.coroutine.android)

    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    implementation(libs.koin.test)
    ksp(libs.koin.ksp.compiler)

    testImplementation(projects.core.datasource.api)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.cio)
    testImplementation(libs.ktor.client.websockets)
    testImplementation(libs.ktor.client.serialization.jvm)
    testImplementation(libs.ktor.serialization.kotlinx.protobuf)
    testImplementation(libs.ktor.client.logging.jvm)

    testImplementation(libs.junit.junit)
}