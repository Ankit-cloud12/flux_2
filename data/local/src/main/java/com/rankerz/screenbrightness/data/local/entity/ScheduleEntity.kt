package com.rankerz.screenbrightness.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rankerz.screenbrightness.core.domain.model.Schedule

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // Auto-generate primary key
    val name: String?, // Allow nullable name
    val startTime: Long,
    val endTime: Long,
    val brightnessLevel: Float?, // Use brightnessLevel from domain model
    val colorTemperatureKelvin: Int?, // Use colorTemperatureKelvin from domain model
    val daysOfWeek: List<Int>, // Stored as String via TypeConverter
    val isEnabled: Boolean,
    val profileId: String?, // Add profileId if schedules link to UserProfiles
    val type: String // Store enum as String (e.g., "TIME", "SUNRISE_SUNSET")
)

// Extension function to map Entity to Domain model
fun ScheduleEntity.toDomainModel(): Schedule {
    return Schedule(
        id = this.id,
        name = this.name, // Pass name through
        startTime = this.startTime,
        endTime = this.endTime,
        brightness = this.brightnessLevel, // Map brightnessLevel to brightness
        colorTemperature = this.colorTemperatureKelvin, // Map colorTemperatureKelvin to colorTemperature
        daysOfWeek = this.daysOfWeek,
        isEnabled = this.isEnabled,
        profileId = this.profileId, // Pass profileId through
        type = try { // Safely convert String back to Enum
            Schedule.ScheduleType.valueOf(this.type)
        } catch (e: IllegalArgumentException) {
            Schedule.ScheduleType.TIME // Default to TIME if conversion fails
        }
    )
}

// Extension function to map Domain model to Entity
fun Schedule.toEntity(): ScheduleEntity {
    return ScheduleEntity(
        id = this.id,
        name = this.name, // Pass name through
        startTime = this.startTime,
        endTime = this.endTime,
        brightnessLevel = this.brightness, // Map brightness to brightnessLevel
        colorTemperatureKelvin = this.colorTemperature, // Map colorTemperature to colorTemperatureKelvin
        daysOfWeek = this.daysOfWeek,
        isEnabled = this.isEnabled,
        profileId = this.profileId, // Pass profileId through
        type = this.type.name // Store enum name as String
    )
}