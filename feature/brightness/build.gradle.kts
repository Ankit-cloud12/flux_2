plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt") // Or use KSP if preferred: id("com.google.devtools.ksp")
}

android {
    namespace = "com.rankerz.screenbrightness.feature.brightness"
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
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Or latest
    implementation("androidx.activity:activity-compose:1.8.2") // Or latest
    implementation(platform("androidx.compose:compose-bom:2024.02.01")) // Or latest BOM
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") // ViewModel scope
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0") // collectAsStateWithLifecycle

    // Hilt
    implementation("com.google.dagger:hilt-android:2.49") // Or latest
    kapt("com.google.dagger:hilt-compiler:2.49") // Or use KSP
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0") // For hiltViewModel()

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // For viewModelScope

    // Project dependencies
    implementation(project(":core:domain"))
    implementation(project(":core:ui")) // Common UI components
    // implementation(project(":core:common")) // Add if common utilities are needed
    // implementation(project(":data:system")) // Add if direct system access is needed (should ideally be through domain/data layers)

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