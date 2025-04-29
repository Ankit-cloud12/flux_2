plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") // Use KSP for Room
    id("com.google.dagger.hilt.android")
    // kotlin("kapt") // Remove kapt if only using KSP
}

android {
    namespace = "com.rankerz.screenbrightness.data.local" // Updated namespace
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
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0") // Or latest

    // Room Persistence Library (Example - adjust versions)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1") // For Flow support & coroutines
    ksp("androidx.room:room-compiler:2.6.1")

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.1.1") // Use latest stable

    // Hilt (Align version with app module)
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-compiler:2.51.1") // Use KSP for Hilt too

    // Project Dependencies
    // api(project(":core:data")) // Expose repository interfaces if implementations are here
    // implementation(project(":core:common"))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// Remove kapt block if not used
// kapt {
//     correctErrorTypes = true
// }