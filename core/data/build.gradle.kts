plugins {
    id("java-library") // Pure Kotlin/Java module
    id("org.jetbrains.kotlin.jvm")
    kotlin("kapt") // Add kapt plugin for Hilt
    // No Android plugins needed
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    // Kotlin standard library
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0") // Match project Kotlin version

    // Coroutines for Flow (if repositories use Flow)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // Or latest

    // Hilt/Javax inject annotations (if needed for repository interfaces/implementations)
    implementation("javax.inject:javax.inject:1")
    // Hilt compiler
    kapt("com.google.dagger:hilt-compiler:2.51.1") // Match app module version

    // Project dependencies
    implementation(project(":core:domain")) // Implement domain interfaces
    // TODO: Add core:common dependency if needed

    testImplementation("junit:junit:4.13.2")
}