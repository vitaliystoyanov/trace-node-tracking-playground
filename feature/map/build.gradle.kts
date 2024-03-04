plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.convention.multiplatform.target.default)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.compose.bom))
            implementation(libs.compose.ui)
            implementation(libs.androidx.animation.core.android)
            implementation(libs.androidx.activityCompose)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiToolingData)
            implementation(libs.compose.uiTooling)

            implementation(libs.accompanist.systemuicontroller)

            implementation(libs.androidx.coreKtx)
            implementation(libs.androidx.appCompat)
            implementation(libs.androidx.foundation.layout.android)
            implementation(libs.androidx.material3.android)
            implementation(libs.androidx.lifecycle.viewmodel.ktx)
            implementation(libs.androidx.lifecycle.viewmodel.savedstate)
            implementation(libs.androidx.lifecycle.viewModelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Mapbox Maps SDK
            implementation(libs.android)
            // Note that Compose extension is compatible with Maps SDK v11.0+.
            implementation(libs.maps.compose)
            implementation(libs.maps.style)

            // Koin
            implementation(libs.koin.android)
            implementation(libs.koin.compose)
        }

        commonMain.dependencies {
            implementation(projects.core.model)
            implementation(projects.core.di)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)

            implementation(libs.koin.core)
            implementation(libs.koin.mp.compose)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation("ca.derekellis.mapbox:compose-mapbox-library")
            implementation(libs.kotlinx.datetime)
        }
        jsMain.dependencies {
            implementation(compose.html.core)
        }
    }
}