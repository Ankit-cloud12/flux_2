plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt") // Or use KSP if preferred: id("com.google.devtools.ksp")
}

android {
    namespace = "com.rankerz.screenbrightness"
    compileSdk = 34 // Use the latest stable SDK

    defaultConfig {
        applicationId = "com.rankerz.screenbrightness"
        minSdk = 26 // Minimum SDK level
        targetSdk = 34 // Target the latest stable SDK
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Set to true for release builds later
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
        kotlinCompilerExtensionVersion = "1.5.1" // Use a version compatible with your Kotlin/Compose setup
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0") // Or latest
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Or latest
    implementation("androidx.activity:activity-compose:1.8.2") // Or latest
    implementation(platform("androidx.compose:compose-bom:2024.02.01")) // Or latest BOM
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.7") // Or latest
    // Hilt Navigation Compose
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") // Or latest

    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.51.1") // Latest stable version
    kapt("com.google.dagger:hilt-compiler:2.51.1") // Latest stable version

    // Project Dependencies
    implementation(project(":feature:brightness"))
    implementation(project(":feature:temperature"))
    implementation(project(":feature:scheduling"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:profiles"))
    implementation(project(":feature:perapp"))
    implementation(project(":core:ui"))
    implementation(project(":core:domain")) // Needed by features
    implementation(project(":core:data"))   // Needed by features/DI
    implementation(project(":core:common")) // If utilities are needed

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.01")) // Or latest BOM
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}