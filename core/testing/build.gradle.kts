plugins {
    alias(libs.plugins.stockviewer.android.library)
}

android {
    namespace = "com.core.testing"
}

dependencies {
    api(project(":core:common"))
    api(project(":core:network"))

    debugApi(libs.androidx.compose.ui.testManifest)

    api(libs.androidx.paging.compose)
    api(libs.androidx.compose.ui.test)
    api(libs.androidx.test.rules)
    api(libs.androidx.core.ktx)
    api(libs.core.testing)
    api(libs.kotlinx.coroutines.test)
    api(libs.mockk)
    api(libs.mockk.android)
    api(libs.mockk.agent)
    api(libs.turbine)
    api(libs.androidx.paging.testing)
    api(libs.kluent.android)
    api(libs.truth)
}