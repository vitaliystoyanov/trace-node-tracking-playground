plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    iosX64()
//    iosArm64()
//    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
//        pod("MapboxMaps") {
//            version = "11.1.0"
//            packageName = "MapBox"
//            // https://kotlinlang.org/docs/native-cocoapods-libraries.html#support-for-objective-c-headers-with-import-directives
//            extraOpts += listOf("-compiler-option", "-fmodules") // TODO look up in docs what it's
//        }
    }

    sourceSets {
        iosMain.dependencies {
            implementation("ca.derekellis.mapbox:compose-mapbox-library")
            implementation(projects.core.di)
            implementation(projects.core.datasource.api)
            implementation(projects.feature.map)

            implementation(libs.koin.core)
            implementation(libs.koin.mp.compose)
        }
    }
}