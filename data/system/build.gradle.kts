plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt") // Or use KSP if preferred: id("com.google.devtools.ksp")
}

android {
    namespace = "com.rankerz.screenbrightness.data.system" // Updated namespace
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

    // Hilt
    implementation("com.google.dagger:hilt-android:2.49") // Or latest
    kapt("com.google.dagger:hilt-compiler:2.49") // Or use KSP

    // TODO: Add project dependencies later
    // api(project(":core:data")) // Expose repository interfaces if implementations are here
    // implementation(project(":core:common"))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}