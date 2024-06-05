plugins {
    alias(libs.plugins.codewars.android.application)
    alias(libs.plugins.codewars.android.application.compose)
    alias(libs.plugins.codewars.android.hilt)
    alias(libs.plugins.codewars.android.application.jacoco)
}

android {
    namespace = "com.test.app.codewars"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.test.app.codewars"
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
    implementation(project(":feature:list"))
    implementation(project(":feature:details"))

    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))

    implementation(libs.navigation.hilt)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.timber)
}
