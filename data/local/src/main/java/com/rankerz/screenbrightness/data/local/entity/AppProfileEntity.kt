package com.rankerz.screenbrightness.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rankerz.screenbrightness.core.domain.model.AppProfile

@Entity(tableName = "app_profiles")
data class AppProfileEntity(
    @PrimaryKey val packageName: String, // Use package name as the primary key
    val appName: String?, // Store app name if available, might be null initially
    val brightnessLevel: Float?, // Nullable if using default system brightness
    val colorTemperatureKelvin: Int? // Nullable if using default system temperature
)

// Extension function to map Entity to Domain model
fun AppProfileEntity.toDomainModel(): AppProfile {
    return AppProfile(
        packageName = this.packageName,
        appName = this.appName,
        brightnessLevel = this.brightnessLevel,
        colorTemperatureKelvin = this.colorTemperatureKelvin
    )
}

// Extension function to map Domain model to Entity
fun AppProfile.toEntity(): AppProfileEntity {
    return AppProfileEntity(
        packageName = this.packageName,
        appName = this.appName,
        brightnessLevel = this.brightnessLevel,
        colorTemperatureKelvin = this.colorTemperatureKelvin
    )
}