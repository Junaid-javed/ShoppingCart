plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "1.9.10"
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.shoppingcart"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.shoppingcart"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.compose.navigation)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation (libs.androidx.foundation)
    implementation(libs.accompanist.swiperefresh)

    testImplementation( "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation ("org.mockito:mockito-core:5.2.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation ("io.insert-koin:koin-test:3.5.0")
    testImplementation ("io.insert-koin:koin-test-junit4:3.5.0")
    testImplementation ("org.mockito:mockito-inline:5.2.0")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")


}