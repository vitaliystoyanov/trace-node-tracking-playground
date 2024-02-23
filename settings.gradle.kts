rootProject.name = "node_traces_streaming"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Mapbox Maven repository
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            // Do not change the username below. It should always be "mapbox" (not your username).
            credentials.username = "mapbox"
            // Use the secret token stored in gradle.properties as the password
            credentials.password = providers.gradleProperty("MAPBOX_DOWNLOADS_TOKEN").get()
            authentication.create<BasicAuthentication>("basic")
        }

    }
}
include(":ktor-server-app")
include(":compose-android-app")

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

