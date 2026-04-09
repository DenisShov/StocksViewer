plugins {
    alias(libs.plugins.stockviewer.android.library)
}

android {
    namespace = "com.test.app.stockviewer.core.testing"
}

dependencies {
    api(project(":core:common"))
    api(project(":core:model"))
    api(project(":core:network"))
    api(project(":core:data"))

    debugApi(libs.androidx.compose.ui.testManifest)

    api(libs.androidx.paging.compose)
    api(libs.androidx.compose.ui.test)
    api(libs.androidx.test.rules)
    api(libs.androidx.core.ktx)
    api(libs.core.testing)
    api(libs.hilt.android.testing)
    api(libs.kotlinx.coroutines.test)
    api(libs.mockk)
    api(libs.turbine)
    api(libs.androidx.paging.testing)
    api(libs.kluent.android)
    api(libs.truth)
}