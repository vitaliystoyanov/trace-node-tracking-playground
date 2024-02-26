plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.convention.android.library)
}

kotlin {
    // Apply the default hierarchy again
    applyDefaultHierarchyTemplate()
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        browser()
//    }
    js(IR) {
        browser()
    }
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(projects.core.domain)
            api(projects.core.data)
            api(projects.core.model)
            api(projects.core.network.websocket.imp.ktor)
            api(projects.core.runtime.configuration)

            implementation(libs.kotlinx.coroutine.core)
            implementation(libs.koin.core)
            implementation(libs.koin.test)
        }
        commonTest.dependencies {
            implementation(projects.core.datasource.api)
            implementation(projects.core.network.websocket.api)
            implementation(projects.core.database.api)
            implementation(projects.core.model)


            implementation(kotlin("reflect"))
            implementation(kotlin("test"))
        }
    }
}