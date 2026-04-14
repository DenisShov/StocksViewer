plugins {
    alias(libs.plugins.stockviewer.android.feature)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.feature.details.api"
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":core:navigation"))
}