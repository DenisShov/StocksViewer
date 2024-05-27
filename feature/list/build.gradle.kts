plugins {
    alias(libs.plugins.codewars.android.feature)
    alias(libs.plugins.codewars.android.library.compose)
    alias(libs.plugins.codewars.android.hilt)
    alias(libs.plugins.codewars.android.library.jacoco)
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
    implementation(project(":core:ui"))

    implementation(libs.androidx.paging.compose)

    testImplementation(project(":core:testing"))
}