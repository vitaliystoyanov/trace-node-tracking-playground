rootProject.name = "node_traces_streaming"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        // https://youtrack.jetbrains.com/issue/KTOR-5587/Ktor-client-for-Kotlin-Wasm#focus=Comments-27-8735419.0-0
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        // Mapbox Maven repository
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            // Do not change the username below. It should always be "mapbox" (not your username).
            credentials.username = "mapbox"
            // Use the secret token stored in gradle.properties as the password
            credentials.password = providers.gradleProperty("MAPBOX_DOWNLOADS_TOKEN").get()
            authentication.create<BasicAuthentication>("basic")
        }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":ktor-server-app")
// See more: https://docs.gradle.org/current/userguide/composite_builds.html
// and Declaring dependencies substituted by an included build:
// https://docs.gradle.org/current/userguide/composite_builds.html#included_build_declaring_substitutions
includeBuild("compose-mapbox-library") {
    dependencySubstitution {
        substitute(module("ca.derekellis.mapbox:compose-mapbox-library")).using(project(":"))
    }
}
includeBuild(".")

include(":compose-android-app")
include(":compose-desktop-app")
include(":compose-web-app")
include(":compose-web-wasm-app")

include(":core:common")
include(":core:model")
include(":core:data")
include(":core:datasource:api")
include(":core:database:api")
include(":core:database:imp:room")
include(":core:designsystem")
include(":core:di")
include(":core:domain")
include(":core:network:websocket:api")
include(":core:network:websocket:imp:ktor")
include(":core:network:websocket:imp:scarlet")
include(":core:runtime:configuration")
include(":core:runtime:logging")
include(":core:runtime:metrics")
include(":core:ui")

include(":feature:map")

include(":shared")
