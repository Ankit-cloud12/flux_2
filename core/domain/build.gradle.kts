plugins {
    id("java-library") // Pure Kotlin/Java module
    id("org.jetbrains.kotlin.jvm")
    // No Android plugins needed
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    // Kotlin standard library
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0") // Match project Kotlin version

    // Coroutines for Flow (if use cases use Flow)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // Or latest

    // Hilt/Javax inject annotations (if needed for use cases)
    implementation("javax.inject:javax.inject:1")

    // TODO: Add project dependencies later (e.g., core:common)

    testImplementation("junit:junit:4.13.2")
}