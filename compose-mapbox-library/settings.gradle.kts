rootProject.name = "compose-mapbox-library"

dependencyResolutionManagement {
    repositories {
        maven("https://androidx.dev/storage/compose-compiler/repository/")
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.mapbox-library.versions.toml"))
        }
    }
}
