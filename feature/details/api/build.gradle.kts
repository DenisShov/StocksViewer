plugins {
    alias(libs.plugins.stockviewer.android.feature)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.test.app.stockviewer.feature.details.api"
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":core:navigation"))

    testImplementation(project(":core:testing"))
}