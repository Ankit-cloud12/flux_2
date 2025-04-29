plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    // No Hilt plugin here, core modules usually don't need it directly
    kotlin("kapt") // Or KSP
}

android {
    namespace = "com.rankerz.screenbrightness.core.ui" // Updated namespace
    compileSdk = 34 // Use the same SDK as the app module

    defaultConfig {
        minSdk = 26 // Use the same minSdk as the app module
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
     buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Match app module
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0") // Or latest
    implementation(platform("androidx.compose:compose-bom:2024.02.01")) // Or latest BOM
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // TODO: Add project dependencies later if needed (e.g., core:common)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.01")) // Or latest BOM
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// Allow references to generated code if needed by dependencies
kapt {
    correctErrorTypes = true
}