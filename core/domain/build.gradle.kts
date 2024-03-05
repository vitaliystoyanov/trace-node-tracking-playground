plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.multiplatform.target.default)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.data)
            implementation(projects.core.common)
            implementation(projects.core.model)
            implementation(projects.core.runtime.logging)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutine.core)
            implementation(libs.koin.core)
        }
    }
}