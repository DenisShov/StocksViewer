plugins {
    alias(libs.plugins.stockviewer.android.feature)
    alias(libs.plugins.stockviewer.android.library.compose)
    alias(libs.plugins.stockviewer.android.hilt)
    alias(libs.plugins.stockviewer.android.library.jacoco)
}

android {
    namespace = "com.test.app.list"
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":core:domain"))
    api(project(":core:ui"))

    implementation(libs.androidx.paging.compose)
    implementation(libs.coil)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}