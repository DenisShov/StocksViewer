plugins {
    alias(libs.plugins.stockviewer.android.application)
    alias(libs.plugins.stockviewer.android.application.compose)
    alias(libs.plugins.stockviewer.android.hilt)
    alias(libs.plugins.stockviewer.android.application.jacoco)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.test.app.stockviewer"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.test.app.stockviewer"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:list:api"))
    implementation(project(":feature:list:impl"))
    implementation(project(":feature:details:api"))
    implementation(project(":feature:details:impl"))

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.timber)
}
