package com.rankerz.screenbrightness.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rankerz.screenbrightness.core.domain.model.UserProfile

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val brightnessLevel: Float,
    val colorTemperatureKelvin: Int
)

// Extension function to map Entity to Domain model
fun UserProfileEntity.toDomainModel(): UserProfile {
    return UserProfile(
        id = this.id,
        name = this.name,
        brightnessLevel = this.brightnessLevel,
        colorTemperatureKelvin = this.colorTemperatureKelvin
    )
}

// Extension function to map Domain model to Entity
fun UserProfile.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = this.id,
        name = this.name,
        brightnessLevel = this.brightnessLevel,
        colorTemperatureKelvin = this.colorTemperatureKelvin
    )
}