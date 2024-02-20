plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
    alias(libs.plugins.kotlin.serialize)
}

group = "io.architecture.playground"
version = "1.0.0"

application {
    mainClass.set("io.architecture.playground.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

//noinspection UseTomlInstead
dependencies {
    implementation(projects.core.network.websocket.api)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.geojson)
    implementation(libs.logback)
    implementation(libs.turf)

    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-protobuf-jvm")
    implementation("io.ktor:ktor-server-websockets-jvm")
    implementation("io.ktor:ktor-server-call-logging")
}